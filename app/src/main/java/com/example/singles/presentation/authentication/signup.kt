package com.example.singles.presentation.authentication

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.util.PacificoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    authViewModel: AuthViewModel,
    onLoginClick: () -> Unit,
    onAgreeClick: () -> Unit // This can navigate to another screen upon successful signup
) {
    val context = LocalContext.current

    // State variables for input fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // State for password visibility
    var confirmPasswordVisible by remember { mutableStateOf(false) } // State for confirm password visibility

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(2.dp))

        // Title
        Text(
            text = "Hot Singles",
            fontSize = 36.sp,
            fontFamily = PacificoFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFBB296)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle between Sign up and Login
        Box(
            modifier = Modifier.border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50))
                        .background(color = Color(0xFFFBB296), shape = RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
                ) {
                    Text(text = "Sign up", color = Color.White)
                }
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Login", color = Color(0xFFFBB296))
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Input Fields
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with Mask/Unmask Icon
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296)),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field with Mask/Unmask Icon
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Retype Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296)),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {
                when {
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                    (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) -> {
                        Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        authViewModel.signUp(email, password,fullName) {
                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT)
                                .show()
                            onAgreeClick() // Navigate to the next screen
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
        ) {
            when (authState) {
                is AuthState.Loading -> CircularProgressIndicator()
//                is AuthState.Success -> Text("Sign In Successful!",color = Color.White)
                is AuthState.Error -> {
                    Text(text = "Sign up", color = Color.White)
                    val errorMessage = (authState as AuthState.Error).message
                    LaunchedEffect(authState) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else ->  Text(text = "Sign up", color = Color.White)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Text
        TextButton(onClick = onLoginClick) {
            Text(
                text = "I already have a profile. Login",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }


    }
}
