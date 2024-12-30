package com.example.singles.presentation.profile


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singles.domain.model.UserProfile
import com.example.singles.data.repository.authentication.AuthRepository
import com.example.singles.data.repository.profile.ProfileRepository
import com.example.singles.presentation.authentication.AuthState
import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository, private val authRepository: AuthRepository) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> get() = _profileState
    // Expose userProfile as a StateFlow to be observed in ProfileScreen
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile
    private val _isImageUploaded = MutableStateFlow<Boolean>(false)
    val isImageUploaded : StateFlow<Boolean> get() = _isImageUploaded



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


    fun getUserProfile() {
        _profileState.value = ProfileState.Loading
        val userId = authRepository.getCurrentUser()
        if (userId != null) {
            viewModelScope.launch {
                val result = authRepository.getUserDocument(userId.uid)
                if (result.isSuccess) {
                    val data = result.getOrNull() as? Map<String, Any>
                    if (data != null) {
                        _userProfile.value = UserProfile(
                            age = data["age"] as String,
                            displayName = data["displayName"] as String,
                            fullName = data["fullName"] as String,
                            gender = data["gender"] as String,
                            image0 = data["image0"] as? String ?: "",
                            image1 = data["image1"] as? String ?: "",
                            image2 = data["image2"] as? String ?: "",
                            image3 = data["image3"] as? String ?: "",
                            institution = data["institution"] as String,
                            email = userId.email ,
                            isAgreed = data["isAgreed"] as Boolean

                        )
                        _profileState.value = ProfileState.Success(_userProfile.value!!)
                    } else {
                        _profileState.value = ProfileState.Error("Failed to parse user profile data")
                    }
                } else {
                    _profileState.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "Failed to get user profile")
                }
            }
        } else {
            _profileState.value = ProfileState.Error("User not found")
        }
    }
    fun updateUserProfileField(fieldKey: String, newValue: Any) {
        val currentProfile = _userProfile.value
        if (currentProfile != null) {
            val updatedProfile = when (fieldKey) {
                "age" -> currentProfile.copy(age = newValue as String)
                "displayName" -> currentProfile.copy(displayName = newValue as String)
                "fullName" -> currentProfile.copy(fullName = newValue as String)
                "gender" -> currentProfile.copy(gender = newValue as String)
                "image0" -> currentProfile.copy(image0 = newValue as String)
                "image1" -> currentProfile.copy(image1 = newValue as String)
                "image2" -> currentProfile.copy(image2 = newValue as String)
                "image3" -> currentProfile.copy(image3 = newValue as String)
                "institution" -> currentProfile.copy(institution = newValue as String)
                "isAgreed" -> currentProfile.copy(isAgreed = newValue as Boolean)
                else -> currentProfile
            }
            _userProfile.value = updatedProfile
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
fun initUpload(){
    _isImageUploaded.value= false
}
    fun uploadImage(imageUri: Uri,index:Int,context: Context ) {
        val userId = (authRepository.getCurrentUser())?.uid
        if (userId != null) {
            _profileState.value = ProfileState.Loading
            viewModelScope.launch {
                val result = profileRepository.uploadImageToStorage(imageUri, userId,index.toString())
                if (result.isSuccess) {
                    _profileState.value = ProfileState.Success(result.getOrNull() ?: "")
                    // Show Toast for upload success or error
                        Toast.makeText(context, "upload successful", Toast.LENGTH_SHORT).show()
                        _isImageUploaded.value = true
                        stopLoader()
                } else {
                    _profileState.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "Failed to upload image")

                        val errorMessage = result.exceptionOrNull()?.message ?: "Failed to upload image"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

                }
            }
        } else {
            _profileState.value = ProfileState.Error("User not authenticated")
        }
    }

    fun stopLoader(){
        _profileState.value= ProfileState.Idle
    }
    fun getUserId():String?{
        return (authRepository.getCurrentUser())?.uid;
    }

    fun logOut(){
        _profileState.value = ProfileState.Loading
         authRepository.signOut();
        _profileState.value= ProfileState.Idle
    }

}

sealed class ProfileState {
    data object Idle : ProfileState()
    data object Loading : ProfileState()
    data class Success(val response: Any) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
