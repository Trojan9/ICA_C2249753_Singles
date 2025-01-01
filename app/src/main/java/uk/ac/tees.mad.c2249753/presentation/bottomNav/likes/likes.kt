package uk.ac.tees.mad.c2249753.presentation.bottomNav.likes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp




@Composable
fun LikesGridScreen(
    modifier: Modifier = Modifier,
    profiles: List<Map<String, Any>>,
    likedProfiles: List<Map<String, Any>>,
    onProfileClick: (String) -> Unit,
    onLikeToggle: ( Map<String, Any>) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Recommend", "Liked")

    Column(modifier = modifier.fillMaxSize()) {
        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tab,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) Color(0xFFFBB296) else Color.Gray
                        )
                    }
                )
            }
        }

        // Profiles Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp)
        ) {
            val displayProfiles = if (selectedTab == 0) {
                profiles
            } else {
                likedProfiles
            }

            items(displayProfiles) { profile ->
                val userId = profile["userId"] as? String ?: return@items
                ProfileCard(
                    profile = profile,
                    isLiked = likedProfiles.any { it["userId"] == userId },
                    onProfileClick = { onProfileClick(userId) },
                    onLikeToggle = { onLikeToggle(profile) }
                )
            }
        }
    }
}
