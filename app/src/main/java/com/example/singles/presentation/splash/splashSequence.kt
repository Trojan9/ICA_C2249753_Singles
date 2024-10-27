package com.example.singles.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.singles.presentation.mainScreen.MainScreen
import kotlinx.coroutines.delay

@Composable
fun SplashSequence() {
    var splashState by remember { mutableIntStateOf(1) } // 1 for first splash, 2 for second splash, 3 for main content

    // Launching a coroutine to manage the splash screen sequence
    LaunchedEffect(Unit) {
        delay(1000) // First splash for 3 seconds
        splashState = 2 // Switch to second splash
        delay(1000) // Second splash for 2 seconds
        splashState = 3 // Switch to main screen after the second splash
    }

    when (splashState) {
        1 -> FirstSplashScreen() // Show the first splash screen
        2 -> SecondSplashScreen() // Show the second splash screen
        3 -> MainScreen() // Transition to main screen (or main app content)
    }
}