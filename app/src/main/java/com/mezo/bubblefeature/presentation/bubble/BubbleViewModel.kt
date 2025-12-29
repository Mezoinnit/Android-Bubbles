package com.mezo.bubblefeature.presentation.bubble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezo.bubblefeature.domain.model.Message
import com.mezo.bubblefeature.domain.repository.ChatRepository
import com.mezo.bubblefeature.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BubbleViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val messages: StateFlow<List<Message>> = chatRepository.getMessages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val isTyping: StateFlow<Boolean> = chatRepository.isTyping()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isDynamicThemeEnabled: StateFlow<Boolean> = settingsRepository.isDynamicThemeEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setChatVisibility(isVisible: Boolean) {
        chatRepository.setChatVisibility(isVisible)
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(text)
        }
    }

    fun clearChat() {
        chatRepository.clearChat()
    }
}