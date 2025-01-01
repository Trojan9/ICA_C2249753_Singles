package uk.ac.tees.mad.c2249753.presentation.authentication

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordPage(
    onNavigateBack: () -> Unit,
    onNavigate: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    val email = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Forgot Password",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFBB296)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFBB296),
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ })
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Password Button
        Button(
            onClick = {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.resetPassword(email.value, onSuccess = {Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                }, onFailure = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
        ) {
            Text(text = "Reset Password", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login
        TextButton(onClick = onNavigateBack) {
            Text(
                text = "Back to Login",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}
