package uk.ac.tees.mad.c2249753.di.factory.profile

import android.content.Context
import uk.ac.tees.mad.c2249753.data.repository.authentication.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.c2249753.data.repository.profile.ProfileRepository
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModelFactory(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore, private val context: Context,) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                ProfileRepository(firebaseAuth,firestore,context),
                AuthRepository(firebaseAuth,firestore)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
