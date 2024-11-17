package com.example.singles.data.repository.nearBy

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NearbyRepository(private val firestore: FirebaseFirestore) {

    suspend fun fetchNearbyProfiles(currentUserId: String): List<Map<String, Any>> {
        // Fetch current user's details to get the institution
        val currentUserSnapshot = firestore.collection("users")
            .document(currentUserId)
            .get()
            .await()

        val institution = currentUserSnapshot.getString("institution") ?: return emptyList()

        // Fetch nearby profiles based on the institution
        val result = firestore.collection("users")
            .whereEqualTo("institution", institution)
            .whereNotEqualTo(FieldPath.documentId(), currentUserId) // Exclude the current user
            .get()
            .await()

        return result.documents.mapNotNull { document ->
            val data = document.data.orEmpty().toMutableMap()
            data["userId"] = document.id // Add the document ID as "userId"
            data
        }
    }


    suspend fun updateMatch(currentUserId: String, matchedUserId: String) {
        firestore.collection("users").document(currentUserId)
            .collection("matches").document(matchedUserId)
            .set(mapOf("matched" to true,"userId" to matchedUserId))
            .await()
    }

    suspend fun updateDislike(currentUserId: String, dislikedUserId: String) {
        firestore.collection("users").document(currentUserId)
            .collection("dislikes").document(dislikedUserId)
            .set(mapOf("disliked" to true))
            .await()
    }


    suspend fun createChat(userId1: String, userId2: String): String {
        // Check if a chat already exists with these participants
        val existingChatQuery = firestore.collection("chats")
            .whereArrayContains("participants", userId1)
            .get()
            .await()

        val existingChat = existingChatQuery.documents.firstOrNull { document ->
            val participants = document.get("participants") as? List<*>
            participants?.contains(userId2) == true
        }

        if (existingChat != null) {
            // If a chat already exists, return its chatId
            return existingChat.id
        }

        // Create chat data
        val chatData = mapOf(
            "participants" to listOf(userId1, userId2),
            "lastMessage" to "",
            "lastTimestamp" to System.currentTimeMillis()
        )

        // Add chat document to Firestore
        val documentReference = firestore.collection("chats")
            .add(chatData)
            .await()

        // Retrieve the generated chatId
        val chatId = documentReference.id

        // Update the chatId in the document
        firestore.collection("chats")
            .document(chatId)
            .update("chatId", chatId)
            .await()

        return chatId
    }
}
