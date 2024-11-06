package com.example.singles.domain.repository.profile

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.io.InputStream

class ProfileRepository(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore, private val context: Context,) {

    private val secondaryStorage: FirebaseStorage

    init {
        // Initialize the secondary Firebase app with manually parsed FirebaseOptions
        val secondaryApp = initializeSecondaryApp(context)

        // Use the secondary app instance to get a FirebaseStorage reference
        secondaryStorage = FirebaseStorage.getInstance(secondaryApp)
    }
    // Function to initialize the secondary Firebase app
    private fun initializeSecondaryApp(context: Context): FirebaseApp {
        // Parse the JSON configuration file from assets
        val inputStream: InputStream = context.assets.open("firebase-ace-stores.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)

        // Extract values from the JSON object to create FirebaseOptions
        val options = FirebaseOptions.Builder()
            .setProjectId(jsonObject.getJSONObject("project_info").getString("project_id"))
            .setApplicationId(jsonObject.getJSONArray("client").getJSONObject(0)
                .getJSONObject("client_info").getString("mobilesdk_app_id"))
            .setApiKey(jsonObject.getJSONArray("client").getJSONObject(0)
                .getJSONArray("api_key").getJSONObject(0).getString("current_key"))
            .setStorageBucket(jsonObject.getJSONObject("project_info").getString("storage_bucket"))
            .build()

        // Initialize and return the secondary Firebase app instance
        return FirebaseApp.initializeApp(context, options, "ace-stores")!!
    }
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

    suspend fun uploadImageToStorage(imageUri: Uri, userId: String,index:String): Result<String> {
        return try {
            // Obtain the current date and time
            val currentDateTime = Date()

// Define a formatter to format the date and time as desired
            val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

// Format the current date and time
            val formattedDateTime = formatter.format(currentDateTime)

// Construct the storage reference with the formatted date and time
            val ref = secondaryStorage.reference.child("user_images/$userId/${formattedDateTime}_${imageUri.lastPathSegment}")
            ref.putFile(imageUri).await()
            val downloadUrl = ref.downloadUrl.await()
            firestore.collection("users").document(userId).update("image$index", downloadUrl).await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}
