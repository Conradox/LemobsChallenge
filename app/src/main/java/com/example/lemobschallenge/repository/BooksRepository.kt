package com.example.lemobschallenge.repository

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.lemobschallenge.R
import com.example.lemobschallenge.api.BookApiInterface
import com.example.lemobschallenge.api.Constants
import com.example.lemobschallenge.api.RetrofitInstancer
import com.example.lemobschallenge.model.Book

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class BooksRepository (context : AppCompatActivity){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), MODE_PRIVATE)

    private suspend fun getBooks() : MutableList<Book>
    {
        val retrofitInstance = RetrofitInstancer.getRetrofitInstance(Constants.BookApiURL)
        val apiInterface = retrofitInstance.create(BookApiInterface::class.java)
        val res = apiInterface.getBooks()

        val books = mutableListOf<Book>()
        val favoriteBooks = getFavoriteBooks()

        res.forEach{
            var favorite : Boolean = false
            favoriteBooks?.let { favBooks ->
                if(favBooks.contains(it.title))
                    favorite = true
            }
            books.add(Book(it.title, it.writer, it.price, it.thumbnailHd, favorite))
        }

        return books
    }

    fun getWalletValue() : Float
    {
        return this.sharedPreferences.getFloat("WalletValue", 100.toFloat())
    }

    fun setWalletValue(newValue : Float)
    {
        this.sharedPreferences.edit().putFloat("WalletValue", newValue).apply()
    }

    private fun getPurchasedBookTitles() : Set<String>?
    {
        return this.sharedPreferences.getStringSet("PurchasedBooks", setOf<String>())
    }

    fun addPurchasedBook(title : String)
    {
        val newSet = getPurchasedBookTitles()?.toMutableSet()
        newSet?.add(title)
        newSet.let {
            this.sharedPreferences.edit().putStringSet("PurchasedBooks", it?.toSet()).apply()
        }
    }

    suspend fun getPurchasedBooks() : MutableList<Book>
    {
        val books = getBooks()
        val purchasedBooksTitles = getPurchasedBookTitles()
        val purchasedBooks = mutableListOf<Book>()

        books.forEach{ book ->
            if(purchasedBooksTitles?.contains(book.title) == true)
            {
                purchasedBooks.add(book)
            }
        }
        return purchasedBooks
    }

    suspend fun getAvailableBooks() : MutableList<Book>
    {
        val availableBooks = getBooks()

        val purchasedBooks = getPurchasedBookTitles()

        val bookList = mutableListOf<Book>()

        availableBooks.forEach{ book ->
            purchasedBooks?.let {
                if(purchasedBooks.contains(book.title))
                {
                    bookList.add(book)
                }
            }
        }
        bookList.forEach{
            availableBooks.remove(it)
        }

        return availableBooks
    }

    private fun getFavoriteBooks() : Set<String>?
    {
        return this.sharedPreferences.getStringSet("FavoriteBooks", setOf<String>())
    }

    fun addFavoriteBook(title : String)
    {
        val newSet = getFavoriteBooks()?.toMutableSet()
        newSet?.add(title)
        newSet.let {
            this.sharedPreferences.edit().putStringSet("FavoriteBooks", it?.toSet()).apply()
        }
    }
    fun removeFavoriteBook(title : String)
    {
        val newSet = getFavoriteBooks()?.toMutableSet()
        newSet?.remove(title)
        newSet.let {
            this.sharedPreferences.edit().putStringSet("FavoriteBooks", it?.toSet()).apply()
        }
    }
}