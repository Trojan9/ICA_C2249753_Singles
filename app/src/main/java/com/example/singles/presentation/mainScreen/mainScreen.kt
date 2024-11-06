package com.example.singles.presentation.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.singles.domain.factory.authentication.AuthViewModelFactory
import com.example.singles.domain.factory.profile.ProfileViewModelFactory
import com.example.singles.presentation.authentication.AuthViewModel
import com.example.singles.presentation.onboarding.OnboardingScreen
import com.example.singles.presentation.profile.ProfileSetupPage
import com.example.singles.presentation.profile.UniversityPage
import com.example.singles.presentation.profile.UploadPhotosPage
import com.example.singles.presentation.profile.VerificationEmailPage
import com.example.singles.presentation.authentication.AuthenticateLayoutPage
import com.example.singles.presentation.authentication.LoginPage
import com.example.singles.presentation.authentication.SignUpPage
import com.example.singles.presentation.authentication.WelcomePage
import com.example.singles.presentation.bottomNavigation
import com.example.singles.presentation.profile.ProfileViewModel
import com.example.singles.ui.theme.SinglesTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun MainScreen() {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(firebaseAuth,firestore)
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(firebaseAuth,firestore,context)
    )
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
                    onAgreeClick = { navController.navigate("welcome") }, authViewModel = authViewModel)


            }
            composable("login") {
                LoginPage(
                    onSignUpClick = { navController.navigate("signup") },
                    onNavigate = { routeString ->
                        navController.navigate(routeString)
                    },
                    authViewModel = authViewModel
                )
            }
            composable("welcome") {
                WelcomePage(  authViewModel = authViewModel,onAgree = { navController.navigate("profileSetup") })
            }
            composable("profileSetup") {
                ProfileSetupPage(navController=navController,onContinueClick = { navController.navigate("verificationEmail") }, profileViewModel = profileViewModel)
            }
            composable("verificationEmail") {
                VerificationEmailPage(onNextClick = { navController.navigate("university") },authViewModel=authViewModel)
            }
            composable("university") {

                UniversityPage(onContinueClick = { navController.navigate("uploadPhotos") },profileViewModel=profileViewModel)
            }
            composable("uploadPhotos") {
                UploadPhotosPage(navController=navController,onContinueClick = { navController.navigate("navBar") },profileViewModel=profileViewModel)
            }
            composable("navBar") {
                bottomNavigation()
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