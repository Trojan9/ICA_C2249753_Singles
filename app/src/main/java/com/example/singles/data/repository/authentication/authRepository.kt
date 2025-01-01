package com.example.singles.data.repository.authentication

import android.util.Log
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

    fun  signOut() {
        firebaseAuth.signOut()

        firestore.clearPersistence()
    }

    fun updateFCM() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            Log.e("AuthRepository", "User is not authenticated. Cannot update FCM token.")
            return
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("AuthRepository", "Fetching FCM token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                if (token.isNullOrEmpty()) {
                    Log.e("AuthRepository", "FCM token is null or empty.")
                    return@addOnCompleteListener
                }

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .update("fcmToken", token)
                    .addOnSuccessListener {
                        Log.d("AuthRepository", "FCM token successfully updated.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("AuthRepository", "Failed to update FCM token in Firestore", e)
                    }
            }
    }


    suspend fun deleteUserDocuments(userId: String) {
        // Delete main user document

            val user = firebaseAuth.currentUser

            user?.let { currentUser ->
                val userId = currentUser.uid

                // Delete chats where participants include the userId
                firestore.collection("chats")
                    .whereArrayContains("participants", userId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val batch = firestore.batch()

                        for (document in querySnapshot.documents) {
                            batch.delete(document.reference)
                        }

                        // Commit batch delete
                        batch.commit()
                            .addOnSuccessListener {
                                // After deleting chats, delete user documents
                                firestore.collection("users").document(userId).delete()
                                    .addOnSuccessListener {
                                        // Finally, delete the authentication user
                                        currentUser.delete().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            } else {
                                                print(task.exception?.message)

                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        print(it.message)
                                    }
                            }
                            .addOnFailureListener {
                                print(it.message)
                            }
                    }
                    .addOnFailureListener {
                        print(it.message)
                    }

        }

//        firestore.collection("users").document(userId).delete().await()

        // Cascade delete subcollections
        val subcollections = listOf("likes", "matches", "messages") // Add your subcollections
        subcollections.forEach { collection ->
            val querySnapshot = firestore.collection(collection).whereEqualTo("userId", userId).get().await()
            querySnapshot.documents.forEach { it.reference.delete().await() }
        }
    }

    suspend fun deleteUserAuthentication(): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("No user logged in"))
        return try {
            firestore.clearPersistence()
            user.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
