package com.example.singles.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomePage(onAgree: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Hot Singles",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFBB296)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Welcome Text
            Text(
                text = "Welcome to Hot Singles.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Guidelines Text
            Text(
                text = "Please take note of these:",
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Safety Guidelines
            Text(
                text = buildAnnotatedString {
                    append("Stay safe. ")
                    addStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.Black,), start = 0, end = 10)
                    append("Don’t be too quick to give out personal information.")
                },
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = buildAnnotatedString {
                    append("Be cool. ")
                    addStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.Black,), start = 0, end = 10)
                    append("Respect your peers and treat them the same way you want to be regarded.")
                },
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )


            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = buildAnnotatedString {
                    append("Be proactive. ")
                    addStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.Black,), start = 0, end = 10)
                    append("In your approach, Respect your peers and treat them the same way you want to be regarded.")
                },
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )



            Spacer(modifier = Modifier.weight(1f))

            // Agree Button
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = "I Agree", color = Color.White)
            }
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Cancel Confirmation", fontSize = 20.sp, color = Color(0xFFFBB296))
                },
                text = {
                    Text("Are you sure you want to cancel this sign up, all previously inputted information will be deleted.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        onAgree() // Navigate to the next page or perform any required action
                    }) {
                        Text("YES", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("NO", fontWeight = FontWeight.Bold, color = Color(0xFFFBB296))
                    }
                }
            )
        }
    }
}
