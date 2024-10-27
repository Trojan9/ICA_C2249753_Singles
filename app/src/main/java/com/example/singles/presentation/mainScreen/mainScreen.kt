package com.example.singles.presentation.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.singles.presentation.onboarding.OnboardingScreen
import com.example.singles.presentation.registration.SignUpPage
import com.example.singles.ui.theme.SinglesTheme

@Composable
fun MainScreen() {
    // Replace this with your main content for the app
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "onboarding"
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    onGetStartedClick = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpPage(

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SinglesTheme {
        MainScreen()
    }
}