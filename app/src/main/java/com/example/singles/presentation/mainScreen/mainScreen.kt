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
import com.example.singles.presentation.profile.ProfileSetupPage
import com.example.singles.presentation.profile.UniversityPage
import com.example.singles.presentation.profile.UploadPhotosPage
import com.example.singles.presentation.profile.VerificationEmailPage
import com.example.singles.presentation.registration.AuthenticateLayoutPage
import com.example.singles.presentation.registration.LoginPage
import com.example.singles.presentation.registration.SignUpPage
import com.example.singles.presentation.registration.WelcomePage
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
                    onGetStartedClick = { navController.navigate("authenticate") }
                )
            }
            composable("authenticate") {
                AuthenticateLayoutPage(
                    onLoginClick = { navController.navigate("login") },
                    onSignUpClick = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpPage(
                    onLoginClick = { navController.navigate("login") },
                    onAgreeClick = { navController.navigate("welcome") },)

            }
            composable("login") {
                LoginPage(
                    onSignUpClick = { navController.navigate("signup") }
                )
            }
            composable("welcome") {
                WelcomePage(onAgree = { navController.navigate("profileSetup") })
            }
            composable("profileSetup") {
                ProfileSetupPage(navController=navController,onContinueClick = { navController.navigate("verificationEmail") })
            }
            composable("verificationEmail") {
                VerificationEmailPage(onNextClick = { navController.navigate("university") })
            }
            composable("university") {
                UniversityPage(onContinueClick = { navController.navigate("uploadPhotos") })
            }
            composable("uploadPhotos") {
                UploadPhotosPage()
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