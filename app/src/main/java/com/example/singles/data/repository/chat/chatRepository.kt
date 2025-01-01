package com.example.singles.data.repository.chat

import com.example.singles.data.dao.MessageDao
import com.example.singles.data.entities.MessageEntity
import com.example.singles.data.network.RetrofitClient
import com.example.singles.domain.model.Chat
import com.example.singles.domain.model.FCMRequest
import com.example.singles.domain.model.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await

class ChatRepository(
    private val firestore: FirebaseFirestore,
    private val messageDao: MessageDao
) {
    private val chatsCollection = firestore.collection("chats")

    // Generate a consistent chatId for two users
    fun generateChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) "${userId1}_${userId2}" else "${userId2}_$userId1"
    }

    fun getMergedMessages(chatId: String): Flow<List<MessageEntity>> {
        return combine(
            getCachedMessages(chatId),
            getMessagesRealtime(chatId)
        ) { cachedMessages, realtimeMessages ->
            val allMessages = (cachedMessages + realtimeMessages)
                .distinctBy { it.timestamp }
                .sortedBy { it.timestamp }

            // Cache new real-time messages
            cacheMessages(realtimeMessages.filter { rtMessage ->
                cachedMessages.none { it.timestamp == rtMessage.timestamp }
            })

            allMessages
        }
    }

    // Fetch user chats
    fun getUserChats(userId: String): Flow<List<Chat>> = callbackFlow {

        val listenerRegistration = firestore.collection("chats")
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents?.mapNotNull { doc ->
                    val participants = doc.get("participants") as? List<String> ?: return@mapNotNull null
                    val otherUserId = participants.firstOrNull { it != userId } ?: return@mapNotNull null
                    Chat(
                        chatId = doc.id,
                        lastMessage = doc.getString("lastMessage") ?: "",
                        lastTimestamp = doc.getLong("lastTimestamp")?:0,
                        otherUserId = otherUserId
                    )
                } ?: emptyList()

                trySend(chats)
            }

        awaitClose { listenerRegistration.remove() }
    }

    // Get messages in real-time as a Flow
    fun getMessagesRealtime(chatId: String): Flow<List<MessageEntity>> = callbackFlow {
        val listenerRegistration = chatsCollection.document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MessageEntity::class.java)?.copy(
                        messageId = doc.id,
                        chatId = chatId
                    )
                }.orEmpty()
                trySend(messages)
            }

        awaitClose { listenerRegistration.remove() }
    }

    // Cache messages into Room
    suspend fun cacheMessages(messages: List<MessageEntity>) {
        messageDao.insertMessages(messages)
    }

    // Fetch cached messages from Room
    fun getCachedMessages(chatId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesByChatId(chatId)
    }

    // Delete messages locally
    suspend fun deleteMessages(chatId: String): Int {
        return messageDao.deleteMessagesByChatId(chatId)
    }

    // Sync Firestore messages to local cache
    suspend fun syncMessagesToCache(chatId: String) {
        val snapshot = chatsCollection.document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .await()

        val messages = snapshot.documents.mapNotNull { doc ->
            doc.toObject(MessageEntity::class.java)?.copy(
                messageId = doc.id,
                chatId = chatId
            )
        }
        cacheMessages(messages)
    }
//
    // Send a new message to Firestore
    suspend fun sendMessage(chatId: String, senderId: String, text: String,millis:Long) {

        val message = mapOf(
            "senderId" to senderId,
            "text" to text,
            "messageId" to millis.toString(),
            "timestamp" to millis,
            "isSeen" to false
        )
        chatsCollection.document(chatId)
            .collection("messages")
            .add(message)
            .await()

        // Update chat metadata
        chatsCollection.document(chatId).set(
            mapOf(
//                "participants" to listOf(senderId),
                "lastMessage" to text,
                "lastTimestamp" to millis
            ),
            com.google.firebase.firestore.SetOptions.merge()
        )
    }

    // Observe chat participants in real-time
    fun getChatParticipants(chatId: String): Flow<List<String>> = callbackFlow {
       try {


           val listenerRegistration = chatsCollection.document(chatId)
               .addSnapshotListener { snapshot, error ->
                   if (error != null) {

                       close(error)
                       return@addSnapshotListener
                   }

                   val participants = snapshot?.get("participants") as? List<String> ?: emptyList()
                   trySend(participants)
               }

           awaitClose { listenerRegistration.remove() }
       }catch (e: Exception) {
           print("outer $e")

       }

    }



    fun getUserDetails(userId: String): Flow<Map<String, Any>?> = callbackFlow {
        val listenerRegistration = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.data)
            }

        awaitClose { listenerRegistration.remove() }
    }

    suspend fun createChat(userId1: String, userId2: String): String {
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
    suspend fun markMessagesAsSeen(chatId: String, userId: String) {
        val messagesQuery = chatsCollection.document(chatId)
            .collection("messages")
            .whereEqualTo("isSeen", false) // Only unread messages
            .whereNotEqualTo("senderId", userId) // Messages not sent by the user
            .get()
            .await()

        val batch = firestore.batch()
        messagesQuery.documents.forEach { document ->
            batch.update(document.reference, "isSeen", true)
        }
        batch.commit().await()
    }

    // Function to check if a chat has unseen messages
    suspend fun hasUnseenMessages(chatId: String): Boolean {
        return try {
            val snapshot = chatsCollection.document(chatId)
                .collection("messages")
                .whereEqualTo("isSeen", false)
                .get()
                .await()

            // Return true if there are unseen messages, false otherwise
            !snapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getRecipientToken(chatId: String, senderId: String): String? {
        val chat = firestore.collection("chats").document(chatId).get().await()
        val participants = chat.get("participants") as List<String>
        val recipientId = participants.firstOrNull { it != senderId }

        return recipientId?.let {
            firestore.collection("users").document(it).get().await()
                .getString("fcmToken") // Ensure the token is stored under "fcmToken"
        }
    }


    suspend fun getUserName(userId: String): String {
        return firestore.collection("users").document(userId).get().await()
            .getString("displayName") ?: "Unknown User"
    }
    suspend fun sendPushNotification(token: String, title: String, body: String, userName: String, chatId: String) {
        val fcmService = RetrofitClient.instance

        val payload = FCMRequest(
            token = token,

                title = title,
                body = body,

            data = mapOf(
                "click_action" to "FLUTTER_NOTIFICATION_CLICK",
                "userName" to userName,
                "chatId" to chatId
            )
        )

        try {
            val response = fcmService.sendNotification(payload)

            if (response.success) {
                println("Notification sent successfully")
            } else {
                println("Failed to send notification")
            }
        } catch (e: Exception) {

            e.printStackTrace()
            println("Error sending notification: ${e.message}")
        }
    }


}
