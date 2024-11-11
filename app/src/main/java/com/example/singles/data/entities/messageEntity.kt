package com.example.singles.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val isSeen: Boolean = false
) {
    // No-argument constructor for Firestore
    constructor() : this("", "", "", "", 0L, false)
}


