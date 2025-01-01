package uk.ac.tees.mad.c2249753.di.factory.chat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.c2249753.data.dao.MessageDao
import uk.ac.tees.mad.c2249753.data.repository.authentication.AuthRepository
import uk.ac.tees.mad.c2249753.data.repository.chat.ChatRepository

import uk.ac.tees.mad.c2249753.presentation.bottomNav.chats.ChatViewModel

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