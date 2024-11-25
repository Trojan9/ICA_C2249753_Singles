package com.example.singles.presentation.bottomNav.likes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProfileDetailScreen(
    profile: Map<String, Any>,
    onBackClick: () -> Unit,
    onLikeToggle: (String) -> Unit,
    isLiked: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Back Button
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        // Profile Image
        AsyncImage(
            model = profile["image0"] as String?,
            contentDescription = "Profile Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        // Name and Age
        Text(
            text = "${profile["displayName"]} / ${profile["age"]}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Gender and About
        Text(text = "Gender", fontWeight = FontWeight.Bold)
        Text(text = profile["gender"] as String)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "About me", fontWeight = FontWeight.Bold)
        Text(text = (profile["aboutMe"] as? String) ?: "")

        // Like Button
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            IconButton(onClick = { onLikeToggle(profile["userId"] as String) }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = if (isLiked) Color(0xFFFBB296) else Color.Gray
                )
            }
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Dislike", tint = Color.Gray)
            }
        }
    }
}
