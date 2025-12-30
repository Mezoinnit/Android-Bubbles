package com.mezo.bubblefeature.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MessageTest {

    @Test
    fun `message initialized with correct values`() {
        val text = "Hello Gemini"
        val sender = "User"
        val message = Message(text = text, sender = sender)

        assertEquals(text, message.text)
        assertEquals(sender, message.sender)
        assertFalse(message.isMezo)
    }

    @Test
    fun `mezo message has correct sender and isMezo flag`() {
        val text = "I am a bubble"
        val message = Message(text = text, sender = "Mezoinnit", isMezo = true)

        assertEquals("Mezoinnit", message.sender)
        assertEquals(true, message.isMezo)
    }
}
