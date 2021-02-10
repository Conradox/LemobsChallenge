package com.example.lemobschallenge.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatActivity
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.repository.BooksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(context : AppCompatActivity) : ViewModel (){

    var bookstoreRepository = BooksRepository(context)
    var availableBooks = MutableLiveData<MutableList<Book>>()
    var showedBooks = MutableLiveData<MutableList<Book>>()
    var purchasedBooks  = MutableLiveData<MutableList<Book>>()
    var walletValue = MutableLiveData<Float>()

    init{
        Log.d("Debugging", "Initializing view model")
        showedBooks.value = mutableListOf<Book>()
        walletValue.value = 0.toFloat()
    }

    fun getAllData()
    {
        getPurchasedBooks()
        getAvailableBooks()
        getWalletValue()

    }

    fun getAvailableBooks()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val books = bookstoreRepository.getAvailableBooks()
            availableBooks.postValue(books)
        }
    }

    private fun getPurchasedBooks()
    {
        CoroutineScope(Dispatchers.IO).launch {
            purchasedBooks.postValue(bookstoreRepository.getPurchasedBooks())

        }
    }

    private fun getWalletValue()
    {
        CoroutineScope(Dispatchers.IO).launch {
            walletValue.postValue(bookstoreRepository.getWalletValue())
        }
    }

    fun createShowedBooks()
    {
        CoroutineScope(Dispatchers.IO).launch {
            showedBooks.value?.clear()
            availableBooks.value?.forEach {
                showedBooks.value?.add(it)
            }
            showedBooks.value?.sortBy { !it.isFavorite }
            notifyDataChanged(showedBooks)
        }
    }

    fun filterShowedBooks(filterText : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            showedBooks.value?.clear()
            if (filterText == "") {

                availableBooks.value?.forEach {

                    showedBooks.value?.add(it)
                }
            } else {
                availableBooks.value?.forEach {
                    if (it.title.toUpperCase().contains(filterText.toUpperCase()))
                        showedBooks.value?.add(it)
                }
            }
            showedBooks.value?.sortBy { !it.isFavorite }
            notifyDataChanged(showedBooks)
        }
    }

    fun buyBook(book:Book) : Int
    {
        if(bookstoreRepository.getWalletValue() >= book.price)
        {
            bookstoreRepository.addPurchasedBook(book.title)

            availableBooks.value?.remove(book)
            showedBooks.value?.remove(book)

            walletValue.value?.let {
                walletValue.value = it - book.price
            }
            walletValue.value?.let {
                bookstoreRepository.setWalletValue(it)
            }

            purchasedBooks.value?.add(book)

            notifyDataChangedS(showedBooks)
            notifyDataChangedS(purchasedBooks)
            return 1
        }else
        {
            return 0
        }
    }

    fun favoriteButton(book : Book)
    {
        if(book.isFavorite == false)
        {
            bookstoreRepository.addFavoriteBook(book.title)
            book.isFavorite = true
        }else
        {
            bookstoreRepository.removeFavoriteBook(book.title)
            book.isFavorite = false
        }
    }

    private suspend fun <T : Any?>notifyDataChanged(data : MutableLiveData<T>){
        withContext(Dispatchers.Main)
        {
            data.value = data.value
        }
    }
    private fun <T : Any?>notifyDataChangedS(data : MutableLiveData<T>) {
        data.value = data.value
    }

}