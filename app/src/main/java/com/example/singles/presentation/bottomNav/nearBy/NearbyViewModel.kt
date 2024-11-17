package com.example.singles.presentation.bottomNav.nearBy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singles.data.repository.nearBy.NearbyRepository
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

    fun fetchNearbyProfiles(currentUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedProfiles = repository.fetchNearbyProfiles(currentUserId)
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
        profileId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (direction == "like") {
                    repository.updateMatch(currentUserId, profileId)
                    repository.createChat(currentUserId, profileId)
                } else {
                    repository.updateDislike(currentUserId, profileId)
                }
                _currentIndex.value = _currentIndex.value + 1
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
        _isLoading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
