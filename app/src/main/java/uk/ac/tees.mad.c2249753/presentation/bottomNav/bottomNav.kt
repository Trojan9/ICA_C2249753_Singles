package uk.ac.tees.mad.c2249753.presentation

import NearbyScreen
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.c2249753.presentation.bottomNav.chats.ChatScreen
import uk.ac.tees.mad.c2249753.presentation.bottomNav.chats.ChatViewModel
import uk.ac.tees.mad.c2249753.presentation.bottomNav.likes.LikesGridScreen
import uk.ac.tees.mad.c2249753.presentation.bottomNav.nearBy.NearbyViewModel
import uk.ac.tees.mad.c2249753.presentation.bottomNav.profile.ProfileScreen
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileViewModel

@Composable
fun bottomNavigation(navController: NavController, profileViewModel: ProfileViewModel,chatViewModel: ChatViewModel,onLogOut: () -> Unit,nearbyViewModel: NearbyViewModel,onDelete: () -> Unit,) {
    var selectedTab by remember { mutableStateOf("Nearby") }
    val currentUserId = profileViewModel.getUserId()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,

                onTabSelected = { tab ->
                    selectedTab = tab
                },
                profileViewModel= profileViewModel,
                chatViewModel = chatViewModel
            )
        }
    ) { innerPadding ->
        when (selectedTab) {
            "Nearby" -> NearbyScreen(modifier = Modifier.padding(innerPadding),profileViewModel=profileViewModel,nearbyViewModel=nearbyViewModel)
            "Likes" ->
            LikesGridScreen(
                profiles = nearbyViewModel.profiles.collectAsState().value,
                likedProfiles = nearbyViewModel.likedProfiles.collectAsState().value,
                onProfileClick = { profileId -> navController.navigate("profile_detail/$profileId") },
                onLikeToggle = { profile ->
                    currentUserId?.let { userId ->
                        nearbyViewModel.toggleLike(currentUserId=   currentUserId, profile =    profile ) // Pass currentUserId
                    }}
            )

            "Chats" -> ChatScreen(modifier = Modifier.padding(innerPadding),navController = navController, chatViewModel = chatViewModel,profileViewModel=profileViewModel)
            "Profile" -> ProfileScreen(modifier = Modifier.padding(innerPadding),profileViewModel=profileViewModel, navController = navController, onLogOut = onLogOut,onDelete=onDelete)
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    profileViewModel: ProfileViewModel,
    chatViewModel: ChatViewModel
) {
    val items = listOf("Nearby", "Likes", "Chats", "Profile")

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item,
                onClick = {
                    profileViewModel.stopLoader()
                    onTabSelected(item)
                        chatViewModel.fetchUserChats(profileViewModel= profileViewModel) // Fetch user's chats on launch

                          },
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