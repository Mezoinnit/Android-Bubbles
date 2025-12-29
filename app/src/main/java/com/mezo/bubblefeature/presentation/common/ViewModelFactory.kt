package com.mezo.bubblefeature.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mezo.bubblefeature.domain.repository.ChatRepository
import com.mezo.bubblefeature.domain.repository.SettingsRepository
import com.mezo.bubblefeature.presentation.bubble.BubbleViewModel
import com.mezo.bubblefeature.presentation.main.MainViewModel

class ViewModelFactory(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BubbleViewModel::class.java)) {
            return BubbleViewModel(chatRepository, settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(chatRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}