package com.mezo.bubblefeature.domain.usecase

import com.mezo.bubblefeature.domain.model.Message
import com.mezo.bubblefeature.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(private val repository: ChatRepository) {
    operator fun invoke(): Flow<List<Message>> = repository.getMessages()
}

class SendMessageUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(text: String) = repository.sendMessage(text)
}

class GetChatVisibilityUseCase(private val repository: ChatRepository) {
    fun setVisible(isVisible: Boolean) = repository.setChatVisibility(isVisible)
}

class TypingStateUseCase(private val repository: ChatRepository) {
    fun isTyping(): Flow<Boolean> = repository.isTyping()
}