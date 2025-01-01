package uk.ac.tees.mad.c2249753.presentation.mainScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import uk.ac.tees.mad.c2249753.data.local.AppDatabase
import uk.ac.tees.mad.c2249753.di.factory.authentication.AuthViewModelFactory
import uk.ac.tees.mad.c2249753.di.factory.chat.ChatViewModelFactory
import uk.ac.tees.mad.c2249753.di.factory.nearby.NearByViewModelFactory
import uk.ac.tees.mad.c2249753.di.factory.profile.ProfileViewModelFactory

import uk.ac.tees.mad.c2249753.presentation.authentication.AuthViewModel
import uk.ac.tees.mad.c2249753.presentation.onboarding.OnboardingScreen
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileSetupPage
import uk.ac.tees.mad.c2249753.presentation.profile.UniversityPage
import uk.ac.tees.mad.c2249753.presentation.profile.UploadPhotosPage
import uk.ac.tees.mad.c2249753.presentation.profile.VerificationEmailPage
import uk.ac.tees.mad.c2249753.presentation.authentication.AuthenticateLayoutPage
import uk.ac.tees.mad.c2249753.presentation.authentication.ForgotPasswordPage
import uk.ac.tees.mad.c2249753.presentation.authentication.LoginPage
import uk.ac.tees.mad.c2249753.presentation.authentication.SignUpPage
import uk.ac.tees.mad.c2249753.presentation.authentication.WelcomePage
import uk.ac.tees.mad.c2249753.presentation.bottomNav.chats.ChatDetailScreen
import uk.ac.tees.mad.c2249753.presentation.bottomNav.chats.ChatViewModel
import uk.ac.tees.mad.c2249753.presentation.bottomNav.likes.ProfileDetailScreen
import uk.ac.tees.mad.c2249753.presentation.bottomNav.nearBy.NearbyViewModel
import uk.ac.tees.mad.c2249753.presentation.bottomNav.profile.PrivacyPolicyPage
import uk.ac.tees.mad.c2249753.presentation.bottomNav.profile.UpdatePhotosPage
import uk.ac.tees.mad.c2249753.presentation.bottomNavigation
import uk.ac.tees.mad.c2249753.presentation.profile.ProfileViewModel
import uk.ac.tees.mad.c2249753.ui.theme.SinglesTheme
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
                composable("forgotten") {
                    ForgotPasswordPage(
                        onNavigateBack = { navController.popBackStack() },
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
                        onNextClick = {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            currentUser?.reload()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (currentUser.isEmailVerified) {
                                        navController.navigate("university")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please verify your email before proceeding.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to check email verification status. Try again later.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        authViewModel = authViewModel
                    )
                }

                composable("privacyPolicy") {
                    PrivacyPolicyPage(
                        onBack = {
                            navController.popBackStack()
                        }
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
                    val currentUserId = profileViewModel.getUserId()

                    if (currentUserId != null) {
                        // Fetch liked profiles only if currentUserId is not null
                        LaunchedEffect(currentUserId) {
                            nearByViewModel.fetchLikedProfiles(currentUserId)
                            println(" this is it $currentUserId")
                                chatViewModel.fetchUserChats(profileViewModel= profileViewModel) // Fetch user's chats on launch

                        }

                        bottomNavigation(
                            profileViewModel = profileViewModel,
                            navController = navController,
                            chatViewModel = chatViewModel,
                            nearbyViewModel = nearByViewModel,
                            onLogOut = {
                                profileViewModel.logOut(
                                    onComplete = {
                                        navController.navigate("login") { popUpTo(0) } // Navigate to login after logout
                                        initializationKey++ // Trigger reinitialization
                                        firestore.clearPersistence()

                                    }
                                )

                            },
                            onDelete = {
                                profileViewModel.deleteAccount(onComplete = {
                                    navController.navigate("login") { popUpTo(0) } // Navigate to login after logout
                                    initializationKey++ // Trigger reinitialization
                                    firestore.clearPersistence()
                                })

                            }
                        )
                    } else {
                        // If user is not logged in, navigate to login
                        LaunchedEffect(Unit) {
                            navController.navigate("login") { popUpTo(0) }
                        }
                    }
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