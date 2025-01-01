package uk.ac.tees.mad.c2249753.domain.model

data class FCMRequest(
    val token: String,
    val title: String,
    val body: String,
    val data: Map<String, String>
)

data class Notification(
    val title: String,
    val body: String
)
data class FCMResponse(
    val success: Boolean,
    val failure: Boolean
)




