package uk.ac.tees.mad.c2249753.data.network

import uk.ac.tees.mad.c2249753.di.apiInterface.FCMService
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
