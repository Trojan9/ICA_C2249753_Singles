package uk.ac.tees.mad.c2249753.presentation.bottomNav.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    navController: NavController,
    userName: String,
    chatId: String,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel
) {
    val messages by chatViewModel.messages.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()
    val errorMessage by chatViewModel.errorMessage.collectAsState()
    var inputMessage by remember { mutableStateOf("") }
    val userId = profileViewModel.getUserId()

    LaunchedEffect(chatId) {
        userId?.let {
            chatViewModel.fetchMessages(chatId) // Fetch messages for the chat
            chatViewModel.markMessagesAsSeen(chatId, it) // Mark messages as seen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text(text = userName, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Handle options click */ }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Options")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        // Messages Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Show loading or error message if applicable
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage ?: "An error occurred", color = Color.Red)
                    }
                }
                else -> {
                    // Show messages or placeholder
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (messages.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "No messages yet.", color = Color.Gray)
                                }
                            }
                        } else {
                            items(messages.size) { index ->
                                val message = messages[index]
                                if (message.senderId == userId) {
                                    SentMessage(
                                        message = message.text,
                                        time = getFormattedTime(message.timestamp)
                                    )
                                } else {
                                    ReceivedMessage(
                                        message = message.text,
                                        time = getFormattedTime(message.timestamp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

if(errorMessage==null) {
    // Input Box (always visible)
    MessageInput(
        inputMessage = inputMessage,
        onInputChanged = { inputMessage = it },
        onSendMessage = {
            if (inputMessage.isNotBlank()) {
                chatViewModel.sendMessage(
                    chatId = chatId,
                    senderId = userId ?: "", // Replace with actual current user ID
                    text = inputMessage
                )
                inputMessage = "" // Clear the input field
            }
        }
    )
}
        }
    }
}

// Helper function to format timestamp into readable time
fun getFormattedTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

