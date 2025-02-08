package com.example.chatinv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatinv.network.ChatRepository
import com.example.chatinv.network.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {
    private val _conversation = MutableLiveData<String>("")
    val conversation: LiveData<String> = _conversation

    private val conversationMessages = mutableListOf<ChatMessage>()
    private val repository = ChatRepository()

    // Hardcode the API key for demo purposes.
    private val API_KEY: String = "API key"

    fun sendUserMessage(message: String) {
        // Add user message to our conversation.
        conversationMessages.add(ChatMessage(role = "user", content = message))
        updateConversationView()

        viewModelScope.launch {
            try {
                // Switch to the IO thread for network operations.
                val response = withContext(Dispatchers.IO) {
                    repository.sendMessage(API_KEY, conversationMessages)
                }
                val botMessage = response.choices.firstOrNull()?.message?.content ?: "No response"
                conversationMessages.add(ChatMessage(role = "assistant", content = botMessage))
                updateConversationView()
            } catch (e: Exception) {
                conversationMessages.add(ChatMessage(role = "assistant", content = "Error: ${e.message}"))
                updateConversationView()
            }
        }
    }

    private fun updateConversationView() {
        val conversationText = conversationMessages.joinToString("\n\n") { msg ->
            when (msg.role) {
                "user" -> "You: ${msg.content}"
                "assistant" -> "Bot: ${msg.content}"
                else -> "${msg.role}: ${msg.content}"
            }
        }
        _conversation.postValue(conversationText)
    }
} 