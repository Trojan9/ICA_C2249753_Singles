package com.example.singles.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.singles.MainActivity
import com.example.singles.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract notification data
        val title = remoteMessage.notification?.title ?: "New Message"
        val message = remoteMessage.notification?.body ?: "You have a new message"
        val chatId = remoteMessage.data["chatId"] // Extract chatId
        val userName = remoteMessage.data["userName"] // Extract userName

        // Show notification
        showNotification(title, message, userName, chatId)
    }

    private fun showNotification(title: String, message: String, userName: String?, chatId: String?) {
        val channelId = "chat_notifications"

        // Intent to navigate to the specific chat screen
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("userName", userName)
            putExtra("chatId", chatId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Chat Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher) // Replace with your notification icon
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Get the current user ID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        // Save the token to Firestore under the user's document
        currentUserId?.let { userId ->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .update("fcmToken", token)
                .addOnSuccessListener {
                    // Log success (optional)
                }
                .addOnFailureListener { e ->
                    // Log failure (optional)
                }
        }
    }
}
