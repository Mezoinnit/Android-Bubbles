package com.mezo.bubblefeature.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezo.bubblefeature.domain.repository.ChatRepository
import com.mezo.bubblefeature.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isDynamicThemeEnabled: StateFlow<Boolean> = settingsRepository.isDynamicThemeEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun areBubblesAllowed(): Boolean = chatRepository.areBubblesAllowed()
    
    fun openSettings() = chatRepository.openNotificationSettings()
    
    fun sendBubbleNotification() = chatRepository.showBubbleNotification()

    fun toggleDynamicTheme(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicThemeEnabled(enabled)
        }
    }
}