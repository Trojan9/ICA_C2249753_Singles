import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.R
import com.example.singles.presentation.profile.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun NearbyScreen(modifier: Modifier, profileViewModel: ProfileViewModel,) {
    var currentIndex by remember { mutableStateOf(0) }
    val profiles = listOf("Ruth", "Wura", "Tolu", "Precious")
    val universityName = "Teesside University"

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotationZ = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
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
                    .padding(horizontal = 16.dp)
                    .aspectRatio(0.75f)
                    .clip(MaterialTheme.shapes.medium)
                    .graphicsLayer(
                        translationX = offsetX.value,
                        translationY = offsetY.value,
                        rotationZ = rotationZ.value
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    when {
                                        offsetX.value > 300f -> {
                                            currentIndex++
                                            resetCard(offsetX, offsetY, rotationZ)
                                        }
                                        offsetX.value < -300f -> {
                                            currentIndex++
                                            resetCard(offsetX, offsetY, rotationZ)
                                        }
                                        else -> {
                                            offsetX.animateTo(0f, animationSpec = tween(300))
                                            offsetY.animateTo(0f, animationSpec = tween(300))
                                            rotationZ.animateTo(0f, animationSpec = tween(300))
                                        }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                    rotationZ.snapTo(offsetX.value / 20)
                                }
                            }
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usermatch),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Profile details overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = profile,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "Female / 21", color = Color.White.copy(alpha = 0.8f))
                    Text(text = "3 km from $universityName", color = Color.White.copy(alpha = 0.8f))
                }

                // Like and Dislike buttons
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { currentIndex++ }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Dislike",
                            tint = Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    IconButton(onClick = { currentIndex++ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Like",
                            tint = Color(0xFFFBB296),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Image dot indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 72.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            contentDescription = null,
                            tint = if (index == 0) Color(0xFFFBB296) else Color.LightGray,
                            modifier = Modifier
                                .size(8.dp)
                                .padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        } else {
            Text(text = "No more profiles", modifier = Modifier.padding(16.dp))
        }
    }
}

suspend fun resetCard(offsetX: Animatable<Float, *>, offsetY: Animatable<Float, *>, rotationZ: Animatable<Float, *>) {
    offsetX.snapTo(0f)
    offsetY.snapTo(0f)
    rotationZ.snapTo(0f)
}
