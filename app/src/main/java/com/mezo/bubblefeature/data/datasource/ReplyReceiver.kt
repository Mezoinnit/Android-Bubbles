package com.mezo.bubblefeature.data.datasource

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.mezo.bubblefeature.BubbleApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyText = remoteInput?.getCharSequence("key_text_reply")?.toString()

        if (replyText != null) {
            val app = context.applicationContext as BubbleApp
            val repository = app.chatRepository
            
            CoroutineScope(Dispatchers.IO).launch {
                repository.sendMessage(replyText)
            }
        }
    }
}