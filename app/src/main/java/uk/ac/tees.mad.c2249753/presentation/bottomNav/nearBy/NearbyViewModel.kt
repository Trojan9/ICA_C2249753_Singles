package uk.ac.tees.mad.c2249753.presentation.bottomNav.nearBy

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import uk.ac.tees.mad.c2249753.data.repository.nearBy.NearbyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NearbyViewModel(private val repository: NearbyRepository) : ViewModel() {
    private val _profiles = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val profiles: StateFlow<List<Map<String, Any>>> = _profiles

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _likedProfiles = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val likedProfiles: StateFlow<List<Map<String, Any>>> = _likedProfiles

    init {

    }

    fun fetchNearbyProfiles(currentUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedProfiles = repository.fetchNearbyProfiles(currentUserId)
                fetchLikedProfiles(currentUserId);
                _profiles.value = fetchedProfiles
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
        _isLoading.value = false
    }

    fun handleSwipe(
        direction: String,
        currentUserId: String,
        profile: Map<String, Any>,
        context: Context // Pass the context to show the toast
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val profileId = profile["userId"] as String

                if (direction == "like") {
                    // Add the like from the current user
                    repository.addLike(currentUserId, profileId)
                    _likedProfiles.value = _likedProfiles.value + profile

                    // Check if the profile user has already liked the current user
                    val isMutualLike = repository.isMutualLike(currentUserId, profileId)

                    if (isMutualLike) {
                        repository.updateMatch(currentUserId, profileId) // Update mutual match
                        repository.createChat(currentUserId, profileId) // Create chat for matched users

                        // Show a toast for a successful match
                        Toast.makeText(
                            context,
                            "You matched with ${profile["displayName"] ?: "a user"}!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Remove the like (if applicable) when swiped left
                    repository.updateDislike(currentUserId, profileId)
                }

                // Move to the next profile
                _currentIndex.value = _currentIndex.value + 1
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }



    fun fetchLikedProfiles(currentUserId: String) {
        viewModelScope.launch {
            try {
                val fetchedLikedProfiles = repository.fetchLikedProfiles(currentUserId)
                _likedProfiles.value = fetchedLikedProfiles
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleLike(currentUserId: String, profile: Map<String, Any>) {
        viewModelScope.launch {
            val profileId = profile["userId"] as? String ?: return@launch
            val isLiked = _likedProfiles.value.any {
                val userId = it["userId"] as? String
                userId == profileId
            }

            if (isLiked) {
                // Remove the like
                repository.removeLike(currentUserId,profileId)
                _likedProfiles.value = _likedProfiles.value.filter {
                    val userId = it["userId"] as? String
                    userId != profileId
                }
            } else {
                // Add the like and save profile details
                repository.addLike(currentUserId, profileId)
                _likedProfiles.value = _likedProfiles.value + profile
            }
        }
    }

}
