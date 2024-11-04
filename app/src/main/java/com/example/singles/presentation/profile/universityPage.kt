package com.example.singles.presentation.profile
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversityPage(onContinueClick: () -> Unit,profileViewModel: ProfileViewModel) {
    var universityName by remember { mutableStateOf("") }
    val profileState by profileViewModel.profileState.collectAsState()
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
            text = "Let's get you connected with your friends. What University do you attend?",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // University Name Input
        OutlinedTextField(
            value = universityName,
            onValueChange = { universityName = it },
            label = { Text("University Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = {
                profileViewModel.updateUniversity(
                    onSuccess = {
                        Toast.makeText(context, "University name updated successfully!", Toast.LENGTH_SHORT).show()
                        onContinueClick() // Navigate to the next page
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }, institution = universityName
                )
                 },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            when (profileState) {
                is ProfileState.Loading -> CircularProgressIndicator()
                is ProfileState.Success -> Text(text = "Continue", color = Color.White)
                is ProfileState.Error -> {
                    Text(text = "Continue", color = Color.White)
                    val errorMessage = (profileState as ProfileState.Error).message
                    LaunchedEffect(profileState) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Text(text = "Continue", color = Color.White)
            }

        }
    }
}
