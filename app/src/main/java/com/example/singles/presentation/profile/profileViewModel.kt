package com.example.singles.presentation.profile


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singles.domain.repository.authentication.AuthRepository
import com.example.singles.domain.repository.profile.ProfileRepository
import com.example.singles.presentation.authentication.AuthState
import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository,private val authRepository: AuthRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> get() = _profileState


    fun updateUserProfile(onSuccess: () -> Unit,onFailure: (String) -> Unit,map: Map<String, Any>) {
        _profileState.value = ProfileState.Loading
        val userId = (authRepository.getCurrentUser())
        if (userId != null) {
            viewModelScope.launch {
                val result = profileRepository.updateUserProfile(userId.uid,map)
                if (result.isSuccess) {
                    _profileState.value= ProfileState.Idle
                    onSuccess()
                } else {
                    onFailure(result.exceptionOrNull()?.message ?: "Failed to update profile")
                    _profileState.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "Failed to update profile")
                }
            }
        } else {
            onFailure("User profile not updated")
            _profileState.value = ProfileState.Error( "User profile not updated")

        }
    }
    fun updateUniversity(onSuccess: () -> Unit,onFailure: (String) -> Unit,institution:String) {
        _profileState.value = ProfileState.Loading
        val userId = (authRepository.getCurrentUser())
        if (userId != null) {
            viewModelScope.launch {
                val result = profileRepository.updateUserInstitution(userId.uid,institution)
                if (result.isSuccess) {
                    _profileState.value= ProfileState.Idle
                    onSuccess()
                } else {
                    onFailure(result.exceptionOrNull()?.message ?: "Failed to update institution")
                    _profileState.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "Failed to update institution")
                }
            }
        } else {
            onFailure("User not authenticated")
            _profileState.value = ProfileState.Error( "User not authenticated")

        }
    }

}

sealed class ProfileState {
    data object Idle : ProfileState()
    data object Loading : ProfileState()
    data class Success(val user: FirebaseUser?) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
