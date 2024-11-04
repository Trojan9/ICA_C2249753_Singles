package com.example.singles.domain.factory.profile

import com.example.singles.domain.repository.authentication.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singles.domain.repository.profile.ProfileRepository
import com.example.singles.presentation.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModelFactory(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(ProfileRepository(firebaseAuth,firestore),AuthRepository(firebaseAuth,firestore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
