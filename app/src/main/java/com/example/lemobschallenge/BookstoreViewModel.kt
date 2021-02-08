package com.example.lemobschallenge

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.repository.BookstoreRepository
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.addFavoriteBook
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.addPurchasedBook
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.getPurchasedBooks
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.getWalletValue
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.removeFavoriteBook
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.setWalletValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookstoreViewModel : ViewModel() {
    lateinit var sharedPreferences : SharedPreferences

    var avaiableBooks = MutableLiveData<MutableList<Book>>()
    var showedBooks = MutableLiveData<MutableList<Book>>()
    var walletValue = MutableLiveData<Float>()

    init{
        Log.d("Debugging", "Initializing view model")
        avaiableBooks.value = mutableListOf<Book>()
        showedBooks.value = mutableListOf<Book>()
        walletValue.value = 0.toFloat()
    }

    fun getAllData()
    {
        CoroutineScope(Dispatchers.IO).launch {

            getAvailableBooks()
            getShowedBooks()
            withContext(Dispatchers.Main)
            {
                showedBooks.value = showedBooks.value
                walletValue.value = getWalletValue()
                showedBooks.value?.sortBy { !it.isFavorite }
            }
        }
    }
    suspend fun getAvailableBooks()
    {
            avaiableBooks.value?.clear()

            var allBooks = BookstoreRepository.getBooks()

            var purchasedBooks = getPurchasedBooks()

            var bookList = mutableListOf<Book>()

            allBooks.forEach{
                val old = it
                purchasedBooks?.let {
                    if(purchasedBooks.contains(old.title))
                    {
                        bookList.add(old)
                    }
                }
            }
            bookList.forEach{
                allBooks.remove(it)
            }

            avaiableBooks.value?.addAll(allBooks)

    }

    suspend fun getShowedBooks(filterText : String = "")
    {
        showedBooks.value?.clear()
        if(filterText == "")
        {

            avaiableBooks.value?.forEach{

                showedBooks.value?.add(it)
            }
        }else{
            avaiableBooks.value?.forEach{
                if(it.title.toUpperCase().contains(filterText.toUpperCase()))
                    showedBooks.value?.add(it)
            }
        }
    }

    fun filterShowedBooks(s : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            getShowedBooks(s)
            withContext(Dispatchers.Main)
            {
                showedBooks.value = showedBooks.value
            }
        }
    }

    fun buy_book(book:Book) : Int
    {
        if(getWalletValue() >= book.price) {
            addPurchasedBook(book.title)
            var auxBook : Book? = null
            avaiableBooks.value?.forEach{
                if( book.title == it.title)
                {
                    auxBook = it
                }
            }

            auxBook?.let {
                avaiableBooks.value?.remove(auxBook)
                showedBooks.value?.remove(auxBook)
            }
            showedBooks.value = showedBooks.value

            walletValue.value?.let {
                walletValue.value = it - book.price
            }
            walletValue.value?.let {
                setWalletValue(it)
            }
            return 1
        }else
        {
            return 0
        }
    }

    fun favorite_buttton(book : Book)
    {
        if(book.isFavorite == false)
        {
            addFavoriteBook(book.title)
            book.isFavorite = true
        }else
        {
            removeFavoriteBook(book.title)
            book.isFavorite = false
        }
    }

    fun putSharedPreferences(shared : SharedPreferences)
    {
        BookstoreRepository.sharedPreferences = shared
    }


}

