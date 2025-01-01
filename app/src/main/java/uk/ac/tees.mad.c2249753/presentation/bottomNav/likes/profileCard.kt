package uk.ac.tees.mad.c2249753.presentation.bottomNav.likes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
fun ProfileCard(
    profile: Map<String, Any>,
    isLiked: Boolean,
    onProfileClick: () -> Unit,
    onLikeToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onProfileClick() }
    ) {
        Box {
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

            // Like Icon
            IconButton(
                onClick = { onLikeToggle() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isLiked) Color(0xFFFBB296) else Color.Gray
                )
            }
        }

        // Profile Name and Age
        Text(
            text = "${profile["displayName"]} / ${profile["age"]}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}
