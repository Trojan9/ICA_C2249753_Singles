package uk.ac.tees.mad.c2249753.presentation.profile

import android.Manifest
import android.net.Uri
import android.provider.MediaStore
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
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import uk.ac.tees.mad.c2249753.R
import java.io.File

@Composable
fun UploadPhotosPage(navController: NavController, onContinueClick: () -> Unit, profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.profileState.collectAsState()
    var isImageUploaded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
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
            PhotoPlaceholder(profileViewModel,0, onUploadDone = {
                isImageUploaded = true
            })
            PhotoPlaceholder(profileViewModel,1,onUploadDone = {
                isImageUploaded = true
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PhotoPlaceholder(profileViewModel,2,onUploadDone = {
                isImageUploaded = true
            })
            PhotoPlaceholder(profileViewModel,3,onUploadDone = {
                isImageUploaded = true
            })
        }

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = {
                if(isImageUploaded){
                onContinueClick()}else{
                    Toast.makeText(context, "Upload at least one image!", Toast.LENGTH_SHORT).show()
                } },
            enabled = isImageUploaded,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = if (profileState is ProfileState.Loading) "Uploading..." else "Continue", color = Color.White)
        }
    }
}
@Composable
fun PhotoPlaceholder(profileViewModel: ProfileViewModel, index: Int,onUploadDone: () -> Unit ) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val profileState by profileViewModel.profileState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            profileViewModel.uploadImage(it, index, context = context) // Start upload
            onUploadDone()
        }
    }

    // Create a temporary file for the captured image
    val tempImageFile = remember {
        File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
            deleteOnExit()
        }
    }

    // Use FileProvider to create a secure URI for the temporary file
    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }
    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            selectedImageUri = tempImageUri
            profileViewModel.uploadImage(tempImageUri, index, context = context)
            onUploadDone()
        }
    }
    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Launch camera if permission is granted
            cameraLauncher.launch(tempImageUri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
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
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Uploaded Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
            else -> {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
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

//        // Show Toast for upload success or error
//        if (profileState is ProfileState.Error) {
//            val errorMessage = (profileState as ProfileState.Error).message
//            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//        } else if (profileState is ProfileState.Success) {
//            Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
//            profileViewModel.stopLoader()
//        }
    }

    // Show the image picker dialog if showDialog is true
    if (showDialog) {
        showImagePickerDialog(
            onGalleryClick = {
                galleryLauncher.launch("image/*")
                showDialog = false // Close dialog after selection
            },
            onCameraClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)

                showDialog = false // Close dialog after selection
            }
        )
    }
}

@Composable
fun showImagePickerDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
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
                        onCameraClick() // Launch camera
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
