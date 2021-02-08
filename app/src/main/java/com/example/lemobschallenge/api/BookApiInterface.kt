package com.example.lemobschallenge.api

import com.example.lemobschallenge.api.model.BooksResponse
import retrofit2.http.GET
interface BookApiInterface {

    @GET("products.json")
    suspend fun getBooks() : BooksResponse
}