package com.example.singles.data.repository.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDocument(userId: String): Result<Map<String, Any>?> {
        return try {
            val documentSnapshot = firestore.collection("users").document(userId).get().await()
            if (documentSnapshot.exists()) {
                Result.success(documentSnapshot.data)
            } else {
                Result.failure(Exception("User document not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addUserToFirestore(userId: String, fullName: String): Result<Unit> {
        return try {
            val userMap = mapOf("fullName" to fullName)
            firestore.collection("users").document(userId).set(userMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun resetPassword(email: String): Task<Void> {
       return firebaseAuth.sendPasswordResetEmail(email);
    }
    suspend fun updateUserAgreement(userId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update("isAgreed", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


     fun sendEmailVerification(): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            user?.sendEmailVerification()
                ?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        // Email sent successfully
                        Result.success(Unit)
                    } else {
                        // Handle error
                        // Return the error if the task failed
                        val exception = verificationTask.exception ?: Exception("Unknown error occurred during email verification")
                        Result.failure<Unit>(exception)
                      }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun signOut() {
        firebaseAuth.signOut()
        firestore.clearPersistence()
    }

suspend fun updateFCM(){
    FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(firebaseAuth.currentUser?.uid!!)
            .update("fcmToken", token)
    }
}


}
