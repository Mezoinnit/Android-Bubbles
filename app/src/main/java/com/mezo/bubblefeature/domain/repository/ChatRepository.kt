package com.mezo.bubblefeature.domain.repository

import com.mezo.bubblefeature.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(): Flow<List<Message>>
    suspend fun sendMessage(text: String)
    fun showBubbleNotification()
    fun areBubblesAllowed(): Boolean
    fun openNotificationSettings()
    fun setChatVisibility(isVisible: Boolean)
    fun isTyping(): Flow<Boolean>
    fun clearChat()
}