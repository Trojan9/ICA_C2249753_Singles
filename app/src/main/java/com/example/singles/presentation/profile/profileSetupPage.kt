package com.example.singles.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.singles.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupPage(navController: NavController,onContinueClick: () -> Unit) {
    var selectedGender by remember { mutableStateOf("GENDER") }
    var age by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Replace with your back icon resource
                    contentDescription = "Back",
                    modifier = Modifier.size(width = 60.dp, height = 30.dp),
                    tint = Color.Black

                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "Hot Singles",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Gender Dropdown
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.Transparent)) {
            OutlinedButton(
                onClick = { isGenderDropdownExpanded = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFBB296))
            ) {
                Text(text = selectedGender, color = Color.Gray, modifier = Modifier.weight(1f))
                DropdownMenu(
                    expanded = isGenderDropdownExpanded,
                    onDismissRequest = { isGenderDropdownExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        selectedGender = "Male"
                        isGenderDropdownExpanded = false
                    },text={
                        Text("Male", color = Color.Gray)
                    })
                    DropdownMenuItem(onClick = {
                        selectedGender = "Female"
                        isGenderDropdownExpanded = false
                    },text={
                        Text("Female", color = Color.Gray)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Age TextField
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("AGE") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display Name TextField
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("DISPLAY NAME") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFBB296))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Conditions Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                colors = CheckboxDefaults.colors(checkmarkColor = Color(0xFFFBB296))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "By signing up, you agree to the terms and conditions for hot single users.",
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Continue Button
        Button(
            onClick = onContinueClick,
            enabled = termsAccepted,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Continue", color = Color.White)
        }
    }
}
