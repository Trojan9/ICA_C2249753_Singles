package com.example.singles.di.factory.nearby

import com.example.singles.data.repository.nearBy.NearbyRepository
import com.example.singles.presentation.bottomNav.nearBy.NearbyViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.google.firebase.firestore.FirebaseFirestore

class NearByViewModelFactory( private val firestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearbyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NearbyViewModel(NearbyRepository(firestore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}