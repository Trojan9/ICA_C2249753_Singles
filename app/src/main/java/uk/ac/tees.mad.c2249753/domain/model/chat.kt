package uk.ac.tees.mad.c2249753.domain.model

data class Chat(
    val chatId: String,
    val lastMessage: String,
    val lastTimestamp: Long,
    val otherUserId: String
)
