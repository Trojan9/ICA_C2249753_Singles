package com.example.singles.presentation.registration

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    onSignUpClick: () -> Unit
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

        Spacer(modifier = Modifier.height(25.dp))

        // Toggle between Sign up and Login
      Box(
          modifier = Modifier .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50)),

      ) {

       Row(
            modifier = Modifier
                .fillMaxWidth()
                ,
            horizontalArrangement = Arrangement.Center
        ) {
           Button(
               onClick = onSignUpClick,
               modifier = Modifier
                   .weight(1f)
                 ,
               shape = RoundedCornerShape(50),
               colors = ButtonDefaults.buttonColors(containerColor = Color.White)
           ) {
               Text(text = "Sign up", color = Color(0xFFFBB296))
           }
           Button(
               onClick = {},
               modifier = Modifier
                   .weight(1f)
                   .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(50)).background(color = Color(0xFFFBB296),shape = RoundedCornerShape(50)),
               shape = RoundedCornerShape(50),
               colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
           ) {
               Text(text = "Login", color = Color.White)
           }
       }}

        Spacer(modifier = Modifier.height(32.dp))

        // Input Fields
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email/Username") },
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
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
        ) {
            Text(text = "Login", color = Color.White)
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
