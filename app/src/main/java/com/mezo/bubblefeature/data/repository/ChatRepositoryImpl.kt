package com.mezo.bubblefeature.data.repository

import com.mezo.bubblefeature.data.datasource.NotificationDataSource
import com.mezo.bubblefeature.domain.model.Message
import com.mezo.bubblefeature.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatRepositoryImpl(
    private val notificationDataSource: NotificationDataSource
) : ChatRepository {

    private val _messages = MutableStateFlow(
        listOf(
            Message(content = "Hello!", isFromMe = false),
            Message(content = "How are you?", isFromMe = false),
            Message(content = "I am Mezoinnit, doing great!", isFromMe = true)
        )
    )

    private val _isTyping = MutableStateFlow(false)
    private var isChatVisible = false
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun getMessages(): Flow<List<Message>> = _messages.asStateFlow()
    override fun isTyping(): Flow<Boolean> = _isTyping.asStateFlow()

    override fun setChatVisibility(isVisible: Boolean) {
        isChatVisible = isVisible
    }

    override fun clearChat() {
        _messages.value = emptyList()
    }

    override suspend fun sendMessage(text: String) {
        val newMessage = Message(content = text, isFromMe = true)
        _messages.update { it + newMessage }
        
        // Simulate organic auto-reply
        repositoryScope.launch {
            delay(1000) // Thinking time
            _isTyping.value = true
            delay(2000) // Typing time
            _isTyping.value = false
            
            val reply = Message(content = "I hear you! \"$text\" sounds very interesting. Let's dive deeper into that.", isFromMe = false)
            _messages.update { it + reply }
            
            if (!isChatVisible) {
                notificationDataSource.showBubbleNotification(_messages.value)
            }
        }
    }

    override fun showBubbleNotification() {
        notificationDataSource.showBubbleNotification(_messages.value)
    }

    override fun areBubblesAllowed(): Boolean = notificationDataSource.areBubblesAllowed()
    override fun openNotificationSettings() = notificationDataSource.openNotificationSettings()
}