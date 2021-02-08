package com.example.lemobschallenge.repository

import android.content.SharedPreferences
import com.example.lemobschallenge.api.BookApiInterface
import com.example.lemobschallenge.api.Constants
import com.example.lemobschallenge.api.RetrofitInstancer
import com.example.lemobschallenge.model.Book

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class BookstoreRepository {
    companion object{
        lateinit var sharedPreferences: SharedPreferences

        suspend fun getBooks() : MutableList<Book>
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

        fun getPurchasedBooks() : Set<String>?
        {
            return this.sharedPreferences.getStringSet("PurchasedBooks", setOf<String>())
        }

        fun addPurchasedBook(title : String)
        {
            var newSet = getPurchasedBooks()?.toMutableSet()
            newSet?.add(title)
            newSet.let {
                this.sharedPreferences.edit().putStringSet("PurchasedBooks", it?.toSet()).apply()
            }
        }

        fun getFavoriteBooks() : Set<String>?
        {
            return this.sharedPreferences.getStringSet("FavoriteBooks", setOf<String>())
        }

        fun addFavoriteBook(title : String)
        {
            var newSet = getFavoriteBooks()?.toMutableSet()
            newSet?.add(title)
            newSet.let {
                this.sharedPreferences.edit().putStringSet("FavoriteBooks", it?.toSet()).apply()
            }
        }
        fun removeFavoriteBook(title : String)
        {
            var newSet = getFavoriteBooks()?.toMutableSet()
            newSet?.remove(title)
            newSet.let {
                this.sharedPreferences.edit().putStringSet("FavoriteBooks", it?.toSet()).apply()
            }
        }
    }
}