package com.mezo.bubblefeature.data.repository

import com.mezo.bubblefeature.data.datasource.NotificationDataSource
import com.mezo.bubblefeature.domain.model.Message
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class ChatRepositoryImplTest {

    private lateinit var repository: ChatRepositoryImpl
    private val notificationDataSource: NotificationDataSource = mock()

    @Before
    fun setup() {
        repository = ChatRepositoryImpl(notificationDataSource)
    }

    @Test
    fun `sendMessage adds message to flow`() = runTest {
        val message = Message("Test message", "User")
        repository.sendMessage(message)

        val messages = repository.getMessages().first()
        assertTrue(messages.contains(message))
        assertEquals(1, messages.size)
    }

    @Test
    fun `clearMessages empties the flow`() = runTest {
        repository.sendMessage(Message("1", "A"))
        repository.sendMessage(Message("2", "B"))
        
        repository.clearMessages()
        
        val messages = repository.getMessages().first()
        assertTrue(messages.isEmpty())
    }
}
