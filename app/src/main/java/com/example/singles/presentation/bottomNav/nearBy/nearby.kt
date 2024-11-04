package com.example.singles.presentation.nearby

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NearbyScreen(modifier: Modifier) {
    var currentIndex by remember { mutableStateOf(0) }
    val profiles = listOf(
        "Ruth",
        "Wura",
        "Tolu",
        "Precious"
    )
    val universityName = "Teesside University"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nearby",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        if (currentIndex < profiles.size) {
            val profile = profiles[currentIndex]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                // Handle drag start if needed
                            },
                            onDragEnd = {
                                // Handle drag end if needed
                            },
                            onDragCancel = {
                                // Handle drag cancel if needed
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                if (dragAmount > 0) {
                                    // Swipe right detected - Like
                                    currentIndex++
                                    println("Swiped Right: Like")
                                } else {
                                    // Swipe left detected - Reject
                                    currentIndex++
                                    println("Swiped Left: Reject")
                                }
                                change.consume() // Consume the gesture to prevent propagation
                            }
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usermatch), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(text = profile, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Female / 21", color = Color.LightGray)
                    Text(text = "2 km $universityName", color = Color.LightGray)
                }
            }
        } else {
            Text(text = "No more profiles", modifier = Modifier.padding(16.dp))
        }

        // Footer navigation icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { /* TODO: Handle Nearby icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.LocationOn, // Replace with appropriate Material icon
                    contentDescription = "Nearby",
                    tint = Color(0xFFFBB296)
                )
            }
            IconButton(onClick = { /* TODO: Handle Likes icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.Favorite, // Replace with appropriate Material icon
                    contentDescription = "Likes",
                    tint = Color(0xFFFBB296)
                )
            }
            IconButton(onClick = { /* TODO: Handle Chats icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.ChatBubble, // Replace with appropriate Material icon
                    contentDescription = "Chats",
                    tint = Color(0xFFFBB296)
                )
            }
            IconButton(onClick = { /* TODO: Handle Profile icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.Person, // Replace with appropriate Material icon
                    contentDescription = "Profile",
                    tint = Color(0xFFFBB296)
                )
            }
        }
    }
}
