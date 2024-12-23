package com.example.singles.presentation.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.singles.data.local.AppDatabase
import com.example.singles.di.factory.authentication.AuthViewModelFactory
import com.example.singles.di.factory.chat.ChatViewModelFactory
import com.example.singles.di.factory.nearby.NearByViewModelFactory
import com.example.singles.di.factory.profile.ProfileViewModelFactory

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
import com.example.singles.presentation.bottomNav.chats.ChatDetailScreen
import com.example.singles.presentation.bottomNav.chats.ChatViewModel
import com.example.singles.presentation.bottomNav.likes.ProfileDetailScreen
import com.example.singles.presentation.bottomNav.nearBy.NearbyViewModel
import com.example.singles.presentation.bottomNav.profile.UpdatePhotosPage
import com.example.singles.presentation.bottomNavigation
import com.example.singles.presentation.profile.ProfileViewModel
import com.example.singles.ui.theme.SinglesTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun MainScreen(userName: String?, chatId: String?) {
    var initializationKey by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Reinitialize FirebaseAuth and Firestore when the key changes
        key(initializationKey) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            // Initialize dependencies
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val messageDao = database.messageDao()

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(firebaseAuth, firestore)
            )
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(firebaseAuth, firestore, context)
            )
            val chatViewModel: ChatViewModel = viewModel(
                factory = ChatViewModelFactory(firestore, messageDao)
            )
            val nearByViewModel: NearbyViewModel = viewModel(
                factory = NearByViewModelFactory(firestore)
            )
            firebaseAuth.currentUser?.reload() // Reload user context after login
            val userId = profileViewModel.getUserId()
            val navController = rememberNavController()

            // NavHost for navigation
            NavHost(
                navController = navController,
                startDestination = if (userId != null) {  if (chatId != null && userName != null) {
                    "chat_detail/$userName/$chatId"
                } else {
                    "navBar"
                } } else "onboarding"
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
                        onAgreeClick = { navController.navigate("welcome") },
                        authViewModel = authViewModel
                    )
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
                    WelcomePage(
                        authViewModel = authViewModel,
                        onAgree = { navController.navigate("profileSetup") }
                    )
                }
                composable("profileSetup") {
                    ProfileSetupPage(
                        navController = navController,
                        onContinueClick = { navController.navigate("verificationEmail") },
                        profileViewModel = profileViewModel
                    )
                }
                composable("verificationEmail") {
                    VerificationEmailPage(
                        onNextClick = { navController.navigate("university") },
                        authViewModel = authViewModel
                    )
                }
                composable("university") {
                    UniversityPage(
                        onContinueClick = { navController.navigate("uploadPhotos") },
                        profileViewModel = profileViewModel
                    )
                }
                composable("uploadPhotos") {
                    UploadPhotosPage(
                        navController = navController,
                        onContinueClick = { navController.navigate("navBar") },
                        profileViewModel = profileViewModel
                    )
                }
                composable("updatePhotos") {
                    UpdatePhotosPage(
                        navController = navController,
                        profileViewModel = profileViewModel
                    )
                }
                composable("navBar") {
                    bottomNavigation(
                        profileViewModel = profileViewModel,
                        navController = navController,
                        chatViewModel = chatViewModel,
                        nearbyViewModel = nearByViewModel,
                        onLogOut = {
                            profileViewModel.logOut()
                            initializationKey++ // Change the key to trigger reinitialization
                        }
                    )
                }
                composable(
                    route = "chat_detail/{userName}/{chatId}",
                    arguments = listOf(
                        navArgument("userName") { defaultValue = "" },
                        navArgument("chatId") { defaultValue = "" }
                    )
                ) { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    val chatId = backStackEntry.arguments?.getString("chatId") ?: ""

                    ChatDetailScreen(
                        navController = navController,
                        userName = userName,
                        chatId = chatId,
                        profileViewModel = profileViewModel,
                        chatViewModel = chatViewModel
                    )
                }




                composable("profile_detail/{profileId}") { backStackEntry ->
                    val profileId = backStackEntry.arguments?.getString("profileId") ?: ""

                    val profiles by nearByViewModel.profiles.collectAsState()
                    val likedProfiles by nearByViewModel.likedProfiles.collectAsState()

                    val profile = profiles.find { it["userId"] == profileId }
                        ?: likedProfiles.find { it["userId"] == profileId }
                    if (profile != null) {
                        ProfileDetailScreen(
                            profile = profile,
                            isLiked = likedProfiles.any { it["userId"] == profileId },
                            onBackClick = { navController.popBackStack() },
                            onLikeToggle = { nearByViewModel.toggleLike(userId!!, profile) }
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Profile not found", color = Color.Gray)
                        }
                    }
                }
            }



        }
    }
    }


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SinglesTheme {
        MainScreen(userName = null,chatId = null )
    }
}

//chat and match making screen