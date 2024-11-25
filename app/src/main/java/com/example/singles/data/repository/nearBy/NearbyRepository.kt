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

    suspend fun fetchLikedProfiles(currentUserId: String): List<Map<String, Any>> {
        // Query the "likesFrom" subcollection under the current user's document
        val likesSnapshot = firestore.collection("likes")
            .document(currentUserId)
            .collection("likesFrom")
            .get()
            .await()

        // Extract the IDs of users the current user has liked
        val likedUserIds = likesSnapshot.documents.mapNotNull { it.id }

        if (likedUserIds.isEmpty()) {
            return emptyList()
        }

        // Fetch the corresponding profiles from the "users" collection
        val profilesSnapshot = firestore.collection("users")
            .whereIn(FieldPath.documentId(), likedUserIds)
            .get()
            .await()

        // Map the profiles to a list of data
        return profilesSnapshot.documents.mapNotNull { document ->
            val profileData = document.data.orEmpty().toMutableMap()
            profileData["userId"] = document.id // Add the document ID as "userId"
            profileData
        }
    }



    suspend fun addLike(currentUserId: String, profileId: String) {
        val likeData = mapOf(
            "likedBy" to currentUserId,
            "timestamp" to System.currentTimeMillis()
        )

        // Add the like to the "likesFrom" subcollection of the profile
        firestore.collection("likes")
            .document(profileId)
            .collection("likesFrom")
            .document(currentUserId)
            .set(likeData)
            .await()
    }

    suspend fun isMutualLike(currentUserId: String, profileId: String): Boolean {
        // Check if the profileId liked the current user
        val mutualLikeSnapshot = firestore.collection("likes")
            .document(currentUserId)
            .collection("likesFrom")
            .document(profileId)
            .get()
            .await()

        return mutualLikeSnapshot.exists()
    }


    suspend fun removeLike(currentUserId: String, profileId: String) {
        // Remove the like from the "likesFrom" subcollection
        firestore.collection("likes")
            .document(profileId)
            .collection("likesFrom")
            .document(currentUserId)
            .delete()
            .await()
    }
}
