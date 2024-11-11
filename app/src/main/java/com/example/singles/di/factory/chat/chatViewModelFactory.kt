package com.example.singles.di.factory.chat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singles.data.dao.MessageDao

import com.example.singles.data.repository.chat.ChatRepository

import com.example.singles.presentation.bottomNav.chats.ChatViewModel

import com.google.firebase.firestore.FirebaseFirestore

class ChatViewModelFactory( private val firestore: FirebaseFirestore,private val messageDao: MessageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(ChatRepository(firestore,messageDao)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}