import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.singles.R
import com.example.singles.presentation.bottomNav.nearBy.NearbyViewModel
import com.example.singles.presentation.profile.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun NearbyScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    nearbyViewModel: NearbyViewModel
) {
    val profiles by nearbyViewModel.profiles.collectAsState()
    val currentIndex by nearbyViewModel.currentIndex.collectAsState()
    val isLoading by nearbyViewModel.isLoading.collectAsState()
    val errorMessage by nearbyViewModel.errorMessage.collectAsState()
    val userId = profileViewModel.getUserId()
    val coroutineScope = rememberCoroutineScope()
    val userProfile by profileViewModel.userProfile.collectAsState()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotationZ = remember { Animatable(0f) }

    LaunchedEffect(userId) {
        userId?.let {
            profileViewModel.getUserProfile()
            nearbyViewModel.fetchNearbyProfiles(it)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading -> {
                // Loading Indicator
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            errorMessage != null -> {
                // Display Error Message
                Text(
                    text = errorMessage ?: "An error occurred",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            profiles.isNotEmpty() && currentIndex < profiles.size -> {
                // Display Profile Cards
                val profile = profiles[currentIndex]
                val profileId = profile["userId"] as? String ?: ""

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
                                                    nearbyViewModel.handleSwipe(
                                                        "like",
                                                        userId!!,
                                                        profileId
                                                    )
                                                    resetCard(offsetX, offsetY, rotationZ)
                                                }
                                                offsetX.value < -300f -> {
                                                    nearbyViewModel.handleSwipe(
                                                        "dislike",
                                                        userId!!,
                                                        profileId
                                                    )
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
                        AsyncImage(
                            model =profile["image0"], // Dynamic URL or Firebase path
                            placeholder = painterResource(id = R.drawable.usermatch), // Placeholder image
                            error = painterResource(id = R.drawable.usermatch), // Error fallback image
                            contentDescription = "Profile Picture",

                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.usermatch), // Replace with actual image from profile
//                            contentDescription = null,
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.fillMaxSize()
//                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = profile["displayName"] as? String ?: "Unknown",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${profile["gender"]} / ${profile["age"]}",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "3 km from ${userProfile?.institution ?: "Unknown"}",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            else -> {
                // No Profiles Available
                Text(
                    text = "No more profiles",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }
    }
}

suspend fun resetCard(offsetX: Animatable<Float, *>, offsetY: Animatable<Float, *>, rotationZ: Animatable<Float, *>) {
    offsetX.snapTo(0f)
    offsetY.snapTo(0f)
    rotationZ.snapTo(0f)
}
