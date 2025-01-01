package uk.ac.tees.mad.c2249753.di.apiInterface

import uk.ac.tees.mad.c2249753.domain.model.FCMRequest
import uk.ac.tees.mad.c2249753.domain.model.FCMResponse
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
