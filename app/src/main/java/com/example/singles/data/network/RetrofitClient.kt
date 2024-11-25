package com.example.singles.data.network

import com.example.singles.di.apiInterface.FCMService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://nodepractice.onrender.com/"

    val instance: FCMService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FCMService::class.java)
    }
}
