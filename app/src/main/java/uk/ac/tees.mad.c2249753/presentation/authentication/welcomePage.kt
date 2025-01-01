package uk.ac.tees.mad.c2249753.presentation.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomePage(authViewModel:AuthViewModel,onAgree: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                    Text(text = "Accept Confirmation", fontSize = 20.sp, color = Color(0xFFFBB296))
                },
                text = {
                    Text("By accepting, you agree to follow the community guidelines and maintain respect for others in your interactions.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        authViewModel.updateUserAgreement(
                            onSuccess = {
                                Toast.makeText(context, "Agreement accepted successfully!", Toast.LENGTH_SHORT).show()
                                onAgree() // Navigate to the next page
                            },
                            onFailure = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
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
