package com.example.singles.presentation.registration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.R
import com.example.singles.ui.theme.SinglesTheme
import com.example.singles.util.PacificoFontFamily

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.signup), // Replace with your actual background image ID
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                ,
//            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Hot Singles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PacificoFontFamily,
                color = Color(0xFFFBB296)
            )

            Spacer(modifier = Modifier.weight(1f)) // Pushes Sign-up button to the bottom

            // Sign Up Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp,).padding(top = 20.dp)
                    .height(48.dp)
                    .border(2.dp, Color(0xFFFBB296), shape = RoundedCornerShape(24.dp)), // Add pink border
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Sign up", color = Color.Black)
            }

            // Login Link Text
            TextButton(onClick = {}) {
                Text(
                    text = "I already have a profile. Login",
                    color = Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPagePreview() {
    SinglesTheme {
        SignUpPage(

        )
    }
}
