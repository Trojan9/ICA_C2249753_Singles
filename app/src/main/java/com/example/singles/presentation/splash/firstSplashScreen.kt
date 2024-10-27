package com.example.singles.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.singles.R
import com.example.singles.ui.theme.SinglesTheme

@Composable
fun FirstSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBB296)) // Adjust to your color preference
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash), // First image resource
            contentDescription = "Hot Singles Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FirstSplashPreview() {
    SinglesTheme {
        FirstSplashScreen()
    }
}