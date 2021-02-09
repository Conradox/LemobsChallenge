package com.example.lemobschallenge

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.repository.BookstoreRepository
import com.example.lemobschallenge.repository.BookstoreRepository.Companion.getPurchasedBooks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel (){
    lateinit var sharedPreferences : SharedPreferences

    var avaiableBooks = MutableLiveData<MutableList<Book>>()
    var showedBooks = MutableLiveData<MutableList<Book>>()
    var purchasedBooks  = MutableLiveData<MutableList<Book>>()
    var walletValue = MutableLiveData<Float>()

    init{
        Log.d("Debugging", "Initializing view model")
        avaiableBooks.value = mutableListOf<Book>()
        showedBooks.value = mutableListOf<Book>()
        purchasedBooks.value = mutableListOf<Book>()
        walletValue.value = 0.toFloat()
    }

    fun getAllData()
    {
        CoroutineScope(Dispatchers.IO).launch {

            getAvailableBooks()
            getShowedBooks()
            getPurshasedBooks()
            withContext(Dispatchers.Main)
            {
                showedBooks.value = showedBooks.value
                walletValue.value = BookstoreRepository.getWalletValue()
                showedBooks.value?.sortBy { !it.isFavorite }
            }
        }
    }
    suspend fun getAvailableBooks()
    {
        avaiableBooks.value?.clear()

        var allBooks = BookstoreRepository.getBooks()

        var purchasedBooks = BookstoreRepository.getPurchasedBooks()

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
        if(BookstoreRepository.getWalletValue() >= book.price) {
            BookstoreRepository.addPurchasedBook(book.title)
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
                BookstoreRepository.setWalletValue(it)
            }

            purchasedBooks.value?.add(book)
            purchasedBooks.value = purchasedBooks.value
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
            BookstoreRepository.addFavoriteBook(book.title)
            book.isFavorite = true
        }else
        {
            BookstoreRepository.removeFavoriteBook(book.title)
            book.isFavorite = false
        }
    }

    fun putSharedPreferences(shared : SharedPreferences)
    {
        BookstoreRepository.sharedPreferences = shared
    }

    fun getPurshasedBooks()
    {
        CoroutineScope(Dispatchers.IO).launch {
            purchasedBooks.value?.clear()

            var books = BookstoreRepository.getBooks()
            var purchasedBooksTitles = getPurchasedBooks()

            books.forEach{ book ->
                purchasedBooks.value?.let {
                    if(purchasedBooksTitles?.contains(book.title) == true)
                    {
                        it.add(book)
                    }
                }
            }
            withContext(Dispatchers.Main)
            {
                purchasedBooks.value = purchasedBooks.value
            }
        }
    }

}