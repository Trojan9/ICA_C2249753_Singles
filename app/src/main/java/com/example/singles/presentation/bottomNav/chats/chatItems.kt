package com.example.singles.presentation.bottomNav.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.singles.R
import com.example.singles.domain.model.ChatItem

@Composable
fun ChatListItem(chat: ChatItem, onClick: () -> Unit,chatViewModel:ChatViewModel) {
    var hasUnseen by remember { mutableStateOf(false) }

    // Fetch unseen message status for this chatId
    LaunchedEffect(chat.chatId) {
        hasUnseen = chatViewModel.checkUnseenMessages(chat.chatId)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        AsyncImage(
            model = chat.imageRes, // Dynamic URL or Firebase path
            placeholder = painterResource(id = R.drawable.usermatch), // Placeholder image
            error = painterResource(id = R.drawable.usermatch), // Error fallback image
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Name, Message, and Status
        Column(
            modifier = Modifier.weight(1f)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

              Text(
                  text = chat.name,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  color = MaterialTheme.colorScheme.onBackground,
                  modifier = Modifier.weight(1f)
//
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = chat.time,
                  fontSize = 12.sp,
                  color = Color.Gray,
                  textAlign = TextAlign.End
              )

          }

            Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = chat.message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Delivery Status Icon
                Icon(
                    imageVector = if (!hasUnseen) {
                       Icons.Filled.DoneAll
                    } else {
                        Icons.Filled.Done // Single tick for sent
                    },
                    contentDescription = "Delivery Status",
                    tint = if (!hasUnseen) Color.Blue else Color.Gray, // Blue for read, Gray otherwise
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Time

    }
}
