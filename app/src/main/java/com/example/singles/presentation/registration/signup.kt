package com.example.singles.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.util.PacificoFontFamily
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    onLoginClick: () -> Unit,
    onAgreeClick: () -> Unit
) {
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
            modifier = Modifier .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50)),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50)).background(color = Color(0xFFFBB296),shape = RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
                ) {
                    Text(text = "Sign up", color = Color.White)
                }
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .weight(1f),
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
            value = "",
            onValueChange = {},
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Retype Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = onAgreeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
        ) {
            Text(text = "Sign up", color = Color.White)
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
