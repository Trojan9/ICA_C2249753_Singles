package com.example.singles.presentation

import NearbyScreen
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.singles.presentation.bottomNav.chats.ChatScreen
import com.example.singles.presentation.bottomNav.chats.ChatViewModel
import com.example.singles.presentation.bottomNav.likes.LikesScreen
import com.example.singles.presentation.bottomNav.profile.ProfileScreen
import com.example.singles.presentation.profile.ProfileViewModel

@Composable
fun bottomNavigation(navController: NavController, profileViewModel: ProfileViewModel,chatViewModel: ChatViewModel) {
    var selectedTab by remember { mutableStateOf("Nearby") }
profileViewModel.getUserProfile()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                }
            )
        }
    ) { innerPadding ->
        when (selectedTab) {
            "Nearby" -> NearbyScreen(modifier = Modifier.padding(innerPadding),profileViewModel=profileViewModel)
            "Likes" -> LikesScreen(modifier = Modifier.padding(innerPadding))
            "Chats" -> ChatScreen(modifier = Modifier.padding(innerPadding),navController = navController, chatViewModel = chatViewModel,profileViewModel=profileViewModel)
            "Profile" -> ProfileScreen(modifier = Modifier.padding(innerPadding),profileViewModel=profileViewModel, navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf("Nearby", "Likes", "Chats", "Profile")

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item,
                onClick = { onTabSelected(item) },
                icon = {
                    when (item) {
                        "Nearby" -> Icon(Icons.Filled.LocationOn, contentDescription = "Nearby", tint = if (selectedTab == item) Color(0xFFFBB296) else Color.Gray)
                        "Likes" -> Icon(Icons.Filled.Favorite, contentDescription = "Likes", tint = if (selectedTab == item) Color(0xFFFBB296) else Color.Gray)
                        "Chats" -> Icon(Icons.Filled.ChatBubble, contentDescription = "Chats", tint = if (selectedTab == item) Color(0xFFFBB296) else Color.Gray)
                        "Profile" -> Icon(Icons.Filled.Person, contentDescription = "Profile", tint = if (selectedTab == item) Color(0xFFFBB296) else Color.Gray)
                    }
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedTab == item) Color(0xFFFBB296) else Color.Gray
                    )
                }
            )
        }
    }
}