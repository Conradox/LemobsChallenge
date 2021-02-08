package com.example.lemobschallenge.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstancer {

    companion object
    {
        fun getRetrofitInstance(url:String) : Retrofit {

            return Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}