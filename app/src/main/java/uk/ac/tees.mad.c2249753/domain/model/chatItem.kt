package uk.ac.tees.mad.c2249753.domain.model

data class ChatItem(
    val chatId: String,
    val name: String,
    val message: String,
    val time: String,
    val imageRes: Any, // URL or Drawable Resource
    val isDelivered: Boolean = false, // Indicates if the message was delivered
    val isRead: Boolean = false // Indicates if the message was read
)
