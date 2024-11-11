package com.example.singles.presentation.bottomNav.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.singles.R
import com.example.singles.presentation.profile.ProfileState
import com.example.singles.presentation.profile.ProfileViewModel
import com.example.singles.domain.model.UserProfile
import java.io.File

@Composable
fun UpdatePhotosPage(navController: NavController, profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.profileState.collectAsState()
    val userProfile by profileViewModel.userProfile.collectAsState()
    profileViewModel.stopLoader()
    var isImageUploaded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    // Update isImageUploaded when profileState indicates a successful upload
    if (profileState is ProfileState.Success) {
        isImageUploaded = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Replace with your back icon resource
                    contentDescription = "Back",
                    modifier = Modifier.size(width = 60.dp, height = 30.dp),
                    tint = Color.Black

                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = "Hot Singles",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Heading
        Text(
            text = "Upload at least 1 Picture of yourself",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Photo Placeholders
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PhotoPlaceholder(profileViewModel, userProfile?.image0, 0)
            PhotoPlaceholder(profileViewModel, userProfile?.image1, 1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PhotoPlaceholder(profileViewModel, userProfile?.image2, 2)
            PhotoPlaceholder(profileViewModel, userProfile?.image3, 3)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = { navController.popBackStack() },
            enabled = isImageUploaded,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Save", color = Color.White)
        }
    }
}

@Composable
fun PhotoPlaceholder(profileViewModel: ProfileViewModel, imageUrl: String?, index: Int) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val profileState by profileViewModel.profileState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            profileViewModel.uploadImage(it, index) // Start upload
        }
    }

    // Temporary URI for storing the captured image from the camera
    val tempImageUri = remember {
        Uri.fromFile(File.createTempFile("temp_image", ".jpg", context.cacheDir))
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            selectedImageUri = tempImageUri
            profileViewModel.uploadImage(tempImageUri, index) // Start upload
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { showDialog = true }, // Show dialog on click
        contentAlignment = Alignment.Center
    ) {
        // Display Upload Progress or Image
        when (profileState) {
            is ProfileState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
            is ProfileState.Success -> {
                // Display uploaded image
                Image(
                    painter = rememberImagePainter(selectedImageUri ?: imageUrl),
                    contentDescription = "Uploaded Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
            else -> {
                Image(
                    painter = rememberImagePainter(data = selectedImageUri ?: imageUrl),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Add icon overlaying the bottom-right corner
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = -(20).dp, y = -(15).dp)
                .size(18.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_photo),
                contentDescription = "Add Photo",
                contentScale = ContentScale.Crop
            )
        }

        // Show Toast for upload success or error
        if (profileState is ProfileState.Error) {
            val errorMessage = (profileState as ProfileState.Error).message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        } else if (profileState is ProfileState.Success) {
            Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
            profileViewModel.stopLoader()
        }
    }

    // Show the image picker dialog if showDialog is true
    if (showDialog) {
        showImagePickerDialog(
            onGalleryClick = {
                galleryLauncher.launch("image/*")
                showDialog = false // Close dialog after selection
            },
            onCameraClick = {
                cameraLauncher.launch(tempImageUri)
                showDialog = false // Close dialog after selection
            },
            tempImageUri = tempImageUri
        )
    }
}

@Composable
fun showImagePickerDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: (Uri) -> Unit,
    tempImageUri: Uri
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Select Image Source") },
            text = {
                Column {
                    TextButton(onClick = {
                        showDialog = false
                        onGalleryClick() // Launch gallery picker
                    }) {
                        Text("Choose from Gallery")
                    }
                    TextButton(onClick = {
                        showDialog = false
                        onCameraClick(tempImageUri) // Launch camera with URI
                    }) {
                        Text("Take a Photo")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
