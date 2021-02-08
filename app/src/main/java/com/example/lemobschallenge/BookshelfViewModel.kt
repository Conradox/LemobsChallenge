package com.example.lemobschallenge

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.repository.BookstoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookshelfViewModel : ViewModel (){

    var purchasedBooks  = MutableLiveData<MutableList<Book>>()
    lateinit var sharedPreferences : SharedPreferences
    var walletValue = MutableLiveData<Float>()

    init{
        purchasedBooks.value = mutableListOf<Book>()
    }
    fun getAllData()
    {
        getPurshasedBooks()
        walletValue.value = BookstoreRepository.getWalletValue()
    }

    fun getPurshasedBooks()
    {
        CoroutineScope(Dispatchers.IO).launch {
            purchasedBooks.value?.clear()

            var books = BookstoreRepository.getBooks()
            var purchasedBooksTitles = getPurchasedBookTitles()

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

    fun getPurchasedBookTitles() : Set<String>?
    {
        return this.sharedPreferences.getStringSet("PurchasedBooks", setOf<String>())
    }

    fun putSharedPreferences(sharedPreferences : SharedPreferences)
    {
        BookstoreRepository.sharedPreferences = sharedPreferences
        this.sharedPreferences =  sharedPreferences
    }
}