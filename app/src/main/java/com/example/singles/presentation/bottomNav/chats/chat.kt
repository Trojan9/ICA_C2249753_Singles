package com.example.singles.presentation.bottomNav.chats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.singles.R
import com.example.singles.domain.model.ChatItem
import com.example.singles.presentation.profile.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel
) {
    val chats by chatViewModel.chats.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()
    val errorMessage by chatViewModel.errorMessage.collectAsState()


    Column(modifier = modifier.fillMaxSize()) {
        // Header
        Text(
            text = "Messages",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        when {
            isLoading -> {
                // Show Loading Indicator
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                // Error Message
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "An error occurred", color = Color.Red)
                }
            }
            chats.isNotEmpty() -> {
                // List of Chats
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chats, key = { it.chatId }) { chat ->
                        val otherUserDetails = chatViewModel.getCachedOtherUserDetails(chat.chatId)

                        if (otherUserDetails == null) {
                            // Show loading for user details
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else {
                            // Render chat item with fetched details
                            ChatListItem(
                                chat = ChatItem(
                                    name = otherUserDetails["displayName"] as? String ?: "Unknown",
                                    message = chat.lastMessage,
                                    chatId = chat.chatId,
                                    time = formatTimestamp(chat.lastTimestamp),
                                    imageRes = otherUserDetails["image0"] as? String ?: R.drawable.usermatch
                                ),
                                chatViewModel=chatViewModel,
                                onClick = {
                                    navController.navigate("chat_detail/${otherUserDetails["displayName"] as? String ?: "Unknown"}/${chat.chatId}")
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                // No chats available
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No chats available.", color = Color.Gray)
                }
            }
        }
    }
}

// Helper function to format the timestamp
fun formatTimestamp(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    val currentCalendar = Calendar.getInstance()
    val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val sdfDay = SimpleDateFormat("EEEE", Locale.getDefault())

    calendar.timeInMillis = timestamp

    // If the timestamp is from today
    if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
    ) {
        return sdfTime.format(Date(timestamp))
    }

    // If the timestamp is from yesterday
    currentCalendar.add(Calendar.DAY_OF_YEAR, -1)
    if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
    ) {
        return "Yesterday"
    }

    // If the timestamp is within this week
    currentCalendar.time = Date()
    currentCalendar.set(Calendar.DAY_OF_WEEK, currentCalendar.firstDayOfWeek)
    val startOfWeek = currentCalendar.timeInMillis

    if (timestamp >= startOfWeek) {
        return sdfDay.format(Date(timestamp))
    }

    // For older dates
    return sdfDate.format(Date(timestamp))
}
