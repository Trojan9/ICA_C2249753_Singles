package com.example.singles.domain.factory.authentication

import com.example.singles.domain.repository.authentication.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singles.presentation.authentication.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModelFactory(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(AuthRepository(firebaseAuth,firestore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
