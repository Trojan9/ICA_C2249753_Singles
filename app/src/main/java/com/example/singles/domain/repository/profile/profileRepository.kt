package com.example.singles.domain.repository.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {


    suspend fun updateUserProfile(userId: String,map: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update(map).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserInstitution(userId: String,institution:String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update("institution", institution).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}
