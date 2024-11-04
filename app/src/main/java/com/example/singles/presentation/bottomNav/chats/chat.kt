package com.example.singles.presentation.bottomNav.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Messages",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        val chats = listOf("Ruth", "Wura", "Tolu", "Precious")

        chats.forEach { name ->
            SwipeToDismiss(
                state = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            // Handle unmatch logic
                        }
                        true
                    }
                ),
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = "Unmatch", color = Color.White, modifier = Modifier.padding(16.dp))
                    }
                },
                dismissContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.usermatch), // Replace with image
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Hello Daniel, I'm $name", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "06:45 pm", color = Color.Gray)
                    }
                }
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}
