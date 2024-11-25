package com.example.singles.di.apiInterface

import com.example.singles.domain.model.FCMRequest
import com.example.singles.domain.model.FCMResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMService {
    @Headers(
        "Content-Type: application/json",
        // Replace with your server key
    )
    @POST("send-notification")
    suspend fun sendNotification(@Body payload: FCMRequest): FCMResponse
}
