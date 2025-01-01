package uk.ac.tees.mad.c2249753.di.factory.authentication

import uk.ac.tees.mad.c2249753.data.repository.authentication.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.c2249753.presentation.authentication.AuthViewModel
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
