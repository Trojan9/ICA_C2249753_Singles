package com.example.singles.presentation.authentication

import com.example.singles.domain.repository.authentication.AuthRepository
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    fun signUp(email: String, password: String, fullName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUpWithEmail(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()
                user?.let {
                    val firestoreResult = authRepository.addUserToFirestore(it.uid, fullName)
                    if (firestoreResult.isSuccess) {
                        _authState.value = AuthState.Success(user)
                        onSuccess() // Navigate to the next page after successful signup and Firestore write
                    } else {
                        _authState.value = AuthState.Error(firestoreResult.exceptionOrNull()?.message ?: "Error storing user data")
                    }
                }
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }
    fun signIn(email: String, password: String, onNavigate: (String) -> Unit, context: Context) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val authResult = authRepository.signInWithEmail(email, password)
            if (authResult.isSuccess) {

                val user = authResult.getOrNull()
                user?.let {
                    val userId = it.uid
                    val userDocumentResult = authRepository.getUserDocument(userId)
                    if (userDocumentResult.isSuccess) {

                        val userData = userDocumentResult.getOrNull()
                        val isAgreed = userData?.get("isAgreed") as? Boolean
                        val age = userData?.get("age") as? String
                        val gender = userData?.get("gender") as? String
                        val university = userData?.get("institution") as? String
                        val isEmailVerified = user.isEmailVerified

                        when {
                            isAgreed == null -> onNavigate("welcome")
                            age == null || gender == null -> onNavigate("profileSetup")
                            !isEmailVerified  -> onNavigate("verificationEmail")
                            university == null -> onNavigate("university")
                            else -> onNavigate("navBar")
                        }
                        Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, userDocumentResult.exceptionOrNull()?.message ?: "Error retrieving user data", Toast.LENGTH_SHORT).show()
                    }
                    _authState.value= AuthState.Success(authResult.getOrNull())
                    _authState.value= AuthState.Idle
                }

            } else {
                _authState.value = AuthState.Error(authResult.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun updateUserAgreement(onSuccess: () -> Unit,onFailure: (String) -> Unit) {
        _authState.value = AuthState.Loading
        val userId = (authRepository.getCurrentUser())
        if (userId != null) {
            viewModelScope.launch {
                val result = authRepository.updateUserAgreement(userId.uid)
                if (result.isSuccess) {
                    _authState.value= AuthState.Idle
                    onSuccess()
                } else {
                    onFailure(result.exceptionOrNull()?.message ?: "Failed to update agreement")
                    _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Failed to update agreement")
                }
            }
        } else {
            onFailure("User not authenticated")
            _authState.value = AuthState.Error( "User not authenticated")

        }
    }

    fun stopLoader() {
        _authState.value= AuthState.Idle
    }
    fun sendEmailVerification() {
        val userEmail =  authRepository.sendEmailVerification();
        if (userEmail.isSuccess) {

            _authState.value= AuthState.Idle
        }else{
            _authState.value = AuthState.Error(userEmail.exceptionOrNull()?.message ?: "email failed")
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
}
