package com.example.singles.presentation.bottomNav.chats

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singles.data.entities.MessageEntity
import com.example.singles.data.repository.chat.ChatRepository
import com.example.singles.domain.model.Chat
import com.example.singles.presentation.profile.ProfileViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()



    private val _participants = MutableStateFlow<List<String>>(emptyList())
    val participants: StateFlow<List<String>> = _participants.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()
    // Fetch user chats

    private val _userDetailsCache = mutableStateOf<Map<String, Map<String, Any>>>(emptyMap())
    val userDetailsCache: Map<String, Map<String, Any>>
        get() = _userDetailsCache.value

    fun getCachedOtherUserDetails(chatId: String): Map<String, Any>? {
        return userDetailsCache[chatId]
    }

    fun fetchUserChats(profileViewModel: ProfileViewModel) {
        val userId = profileViewModel.getUserId()
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _isLoading.value = true
                _userDetailsCache.value = emptyMap()
                _chats.value = emptyList()
                // Fetch all user chats
                chatRepository.getUserChats(userId!!).collect { chats ->

                        _chats.value =  _chats.value + chats
                    // Fetch other user details for all chats
                    viewModelScope.launch {
                        val userDetailsList = chats.map { chat ->
                            async {
                                val participants = chatRepository.getChatParticipants(chat.chatId).firstOrNull()
                                val otherUserId = participants?.firstOrNull { it != userId }

                                otherUserId?.let { id ->
                                    chatRepository.getUserDetails(id).firstOrNull()?.let { userDetails ->
                                        chat.chatId to userDetails
                                    }
                                }
                            }
                        }.awaitAll()

                        // Update cache with non-null results
                        userDetailsList.filterNotNull().forEach { (chatId, userDetails) ->
                            _userDetailsCache.value = _userDetailsCache.value + (chatId to userDetails)
                        }
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                print("this is it 1 $userId $e")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
        _isLoading.value = false
    }

    // Fetch messages
    fun fetchMessages(chatId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value=null

                // Combine cached and real-time messages
                chatRepository.getMergedMessages(chatId).collect { mergedMessages ->
                    // Update the state with merged messages
                    _messages.value = mergedMessages
                }
            } catch (e: Exception) {
                print("this is it 2 $e")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
        _isLoading.value = false
    }



    // Send message
    fun sendMessage(chatId: String, senderId: String, text: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val millis=System.currentTimeMillis();
                // Create a new message
                val newMessage = MessageEntity(
                    messageId = millis.toString(), // Unique message ID
                    chatId = chatId,
                    senderId = senderId,
                    text = text,
                    timestamp = millis,
                    isSeen = false
                )

                // Update Firebase with the new message
                chatRepository.sendMessage(chatId, senderId, text,millis)

                // Update the local cache
                chatRepository.cacheMessages(listOf(newMessage))

                // Update the UI state
                _messages.value = _messages.value + newMessage

                // Get recipient's token (ensure `getRecipientToken` is implemented in your repository)
                val recipientToken = chatRepository.getRecipientToken(chatId, senderId)
                // If a token exists, send a push notification
//                if (recipientToken != null) {
                    val senderName = chatRepository.getUserName(senderId) // Fetch sender's name
                    chatRepository.sendPushNotification(
                        token = recipientToken!!,
                        title = "New Message from $senderName",
                        body = text,
                        userName= senderName,
                        chatId = chatId

                    )
//                }

            } catch (e: Exception) {
                print("this is it 3 $e")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
        _isLoading.value = false
    }


    // Observe participants
    fun observeParticipants(chatId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                chatRepository.getChatParticipants(chatId).collect { participants ->
                    _participants.value = participants
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getOtherUserDetails(chatId: String, currentUserId: String, onResult: (Map<String, Any>?) -> Unit) {
        viewModelScope.launch {
            try {
                chatRepository.getChatParticipants(chatId).collect { participants ->
                    val otherUserId = participants.firstOrNull { it != currentUserId }
                    otherUserId?.let { userId ->
                        chatRepository.getUserDetails(userId).collect { userDetails ->
                            onResult(userDetails)
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }

    fun markMessagesAsSeen(chatId: String, userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                chatRepository.markMessagesAsSeen(chatId, userId)
            } catch (e: Exception) {
                print("this is it 4 $e")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    suspend fun checkUnseenMessages(chatId: String): Boolean {
        return try {
            chatRepository.hasUnseenMessages(chatId)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
