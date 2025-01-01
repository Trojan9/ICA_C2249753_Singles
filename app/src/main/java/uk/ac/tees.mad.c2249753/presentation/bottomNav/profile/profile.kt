package uk.ac.tees.mad.c2249753.presentation.bottomNav.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.c2249753.R
import uk.ac.tees.mad.c2249753.presentation.profile.DeleteState
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileState
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    onLogOut:()->Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {

    val profileState by profileViewModel.profileState.collectAsState()
    val userProfile by profileViewModel.userProfile.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogValue by remember { mutableStateOf("") }
    var dialogFieldKey by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val deleteState by profileViewModel.deleteState.observeAsState()

    // Handle Delete State
    when (deleteState) {
        is DeleteState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DeleteState.Success -> {
            Toast.makeText(LocalContext.current, "Account deleted successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo(0)
            }
            profileViewModel.stopLoader()
        }

        is DeleteState.Error -> {
            val message = (deleteState as DeleteState.Error).message
            Toast.makeText(LocalContext.current, message, Toast.LENGTH_LONG).show()
            profileViewModel.stopLoader()
        }

        else -> Unit // No action needed for other states
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image Section
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    // Handle profile image change here
                }
        ) {
            when (profileState) {
                is ProfileState.Loading -> CircularProgressIndicator()
                else -> {
                    AsyncImage(
                        model = userProfile?.image0,
                        contentDescription = "Profile Image",
                        placeholder = painterResource(id =
                            R.drawable.neutral_avatar
                        ), // Placeholder image
                        error = painterResource(id =
                            R.drawable.neutral_avatar
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
//                    Image(
//                        painter = painterResource(id = R.drawable.usermatch),
//                        contentDescription = "Profile Picture",
//                        modifier = Modifier.fillMaxSize()
//                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Edit Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Fields
        ProfileField(
            icon = Icons.Default.Photo,
            title = "Photos",
            value = "",
            onClick = {
                navController.navigate("updatePhotos")
            }
        )

        ProfileField(
            icon = Icons.Default.Transgender,
            title = "Gender",
            value = userProfile?.gender ?: "",
            onClick = {
                dialogTitle = "Gender"
                dialogValue = userProfile?.gender ?: ""
                dialogFieldKey = "gender"
                showDialog = true
            }
        )

        ProfileField(
            icon = Icons.Default.Person,
            title = "Age",
            value = userProfile?.age ?: "",
            onClick = {
                dialogTitle = "Age"
                dialogValue = userProfile?.age ?: ""
                dialogFieldKey = "age"
                showDialog = true
            }
        )

        ProfileField(
            icon = Icons.Default.Person,
            title = "Display Name",
            value = userProfile?.displayName ?: "",
            onClick = {
                dialogTitle = "Display Name"
                dialogValue = userProfile?.displayName ?: ""
                dialogFieldKey = "displayName"
                showDialog = true
            }
        )

        ProfileField(
            icon = Icons.Default.Person,
            title = "Full Name",
            value = userProfile?.fullName ?: "",
            onClick = {
                dialogTitle = "Full Name"
                dialogValue = userProfile?.fullName ?: ""
                dialogFieldKey = "fullName"
                showDialog = true
            }
        )

        ProfileField(
            icon = Icons.Default.Email,
            title = "Email",
            value = userProfile?.email ?: "",
            onClick = {}, // No action for email since it's uneditable
            editable = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Privacy Policy Button
        Button(
            onClick = {
                navController.navigate("privacyPolicy")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Privacy Policy", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log Out and Delete Account Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onLogOut,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Log Out", color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { showConfirmationDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text("Delete Account", color = Color.White)
            }

            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text(text = "Delete Account") },
                    text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDelete() // Call the delete function
                                showConfirmationDialog = false

                            }
                        ) {
                            Text("Yes, Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showConfirmationDialog = false }
                        ) {
                            Text("Cancel", color = Color.Gray)
                        }
                    }
                )
            }
        }

    }

    // Show dialog for editing profile fields
    if (showDialog) {
        EditFieldDialog(
            title = dialogTitle,
            initialValue = dialogValue,
            onDismiss = { showDialog = false },
            onSave = { newValue ->
                val fieldMap = mapOf(dialogFieldKey to newValue)
                profileViewModel.updateUserProfileField(dialogFieldKey,newValue)
                updateProfileField(fieldMap, profileViewModel, context)

                showDialog = false
            }
        )
    }

    // Show loading indicator when profile update is in progress
    if (profileState is ProfileState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProfileField(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
    editable: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = editable) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFFBB296),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.weight(2f)
        )

        if (editable) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit $title",
                tint = Color(0xFFFBB296),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EditFieldDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
if(title=="Gender") {
    OutlinedButton(
        onClick = { isGenderDropdownExpanded = true },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFBB296))
    ) {
        Text(text = text, color = Color.Gray, modifier = Modifier.weight(1f))
        DropdownMenu(
            expanded = isGenderDropdownExpanded,
            onDismissRequest = { isGenderDropdownExpanded = false }
        ) {
            DropdownMenuItem(onClick = {
                text = "Male"
                isGenderDropdownExpanded = false
            }, text = {
                Text("Male", color = Color.Gray)
            })
            DropdownMenuItem(onClick = {
                text = "Female"
                isGenderDropdownExpanded = false
            }, text = {
                Text("Female", color = Color.Gray)
            })
        }
    }
}else{
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = title) },
                keyboardOptions =if(title=="Age"){ KeyboardOptions(keyboardType = KeyboardType.Number)}else{KeyboardOptions(keyboardType = KeyboardType.Text)},
                singleLine = true
            )
        }},
        confirmButton = {
            TextButton(onClick = { onSave(text) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    )
}

fun updateProfileField(
    valueMap: Map<String, String>,
    profileViewModel: ProfileViewModel,
    context: android.content.Context
) {
    profileViewModel.updateUserProfile(
        map = valueMap,
        onSuccess = {
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        },
        onFailure = { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    )
}
