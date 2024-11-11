package com.example.singles.domain.model

data class Chat(
    val chatId: String,
    val lastMessage: String,
    val lastTimestamp: Long,
    val otherUserId: String
)
