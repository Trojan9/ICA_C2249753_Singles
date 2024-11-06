package com.example.singles.presentation.authentication

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.util.PacificoFontFamily
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    onSignUpClick: () -> Unit,
    onNavigate: (String) -> Unit,
    authViewModel: AuthViewModel
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) } // Toggle for password visibility
    val keyboardController = LocalSoftwareKeyboardController.current
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

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

        Spacer(modifier = Modifier.height(25.dp))

        // Toggle between Sign up and Login
        Box(
            modifier = Modifier
                .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onSignUpClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Sign up", color = Color(0xFFFBB296))
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50))
                        .background(color = Color(0xFFFBB296), shape = RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
                ) {
                    Text(text = "Login", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email/Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with Toggle
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296)),
            visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(imageVector = icon, contentDescription = if (isPasswordVisible.value) "Hide Password" else "Show Password")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password Text
        TextButton(onClick = {}) {
            Text(
                text = "Forgot password? Reset",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                when {
                    !Patterns.EMAIL_ADDRESS.matcher(email.value).matches() -> {
                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    }
                    (email.value.isEmpty() || password.value.isEmpty()) -> {
                        Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        authViewModel.signIn(
                            email.value,
                            password.value,
                            onNavigate = onNavigate,
                            context = context
                        )
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
                is AuthState.Success -> Text("Sign In Successful!", color = Color.White)
                is AuthState.Error -> {
                    Text(text = "Login", color = Color.White)
                    val errorMessage = (authState as AuthState.Error).message
                    LaunchedEffect(authState) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Text(text = "Login", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        TextButton(onClick = onSignUpClick) {
            Text(
                text = "Create Account? Click Here",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}
