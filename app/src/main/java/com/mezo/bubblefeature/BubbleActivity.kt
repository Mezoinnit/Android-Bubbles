package com.mezo.bubblefeature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue // Added explicit import for delegate
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mezo.bubblefeature.presentation.bubble.BubbleViewModel
import com.mezo.bubblefeature.presentation.bubble.ChatScreen
import com.mezo.bubblefeature.presentation.common.ViewModelFactory
import com.mezo.bubblefeature.ui.theme.BubbleFeatureTheme

class BubbleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val app = application as BubbleApp
        val chatRepository = app.chatRepository
        val settingsRepository = app.settingsRepository
        
        setContent {
            val viewModel: BubbleViewModel = viewModel(
                factory = ViewModelFactory(chatRepository, settingsRepository)
            )
            
            val isDynamicTheme by viewModel.isDynamicThemeEnabled.collectAsStateWithLifecycle()

            DisposableEffect(Unit) {
                viewModel.setChatVisibility(true)
                onDispose {
                    viewModel.setChatVisibility(false)
                }
            }

            BubbleFeatureTheme(dynamicColor = isDynamicTheme) {
                ChatScreen(viewModel = viewModel, onClose = { finish() })
            }
        }
    }
}