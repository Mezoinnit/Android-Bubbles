package com.mezo.bubblefeature

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mezo.bubblefeature.presentation.common.ViewModelFactory
import com.mezo.bubblefeature.presentation.main.MainViewModel
import com.mezo.bubblefeature.ui.theme.BubbleFeatureTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        checkAndRequestNotificationPermission()

        val app = application as BubbleApp
        val chatRepository = app.chatRepository
        val settingsRepository = app.settingsRepository

        setContent {
            val viewModel: MainViewModel = viewModel(
                factory = ViewModelFactory(chatRepository, settingsRepository)
            )
            
            val isDynamicTheme by viewModel.isDynamicThemeEnabled.collectAsStateWithLifecycle()

            BubbleFeatureTheme(dynamicColor = isDynamicTheme) {
                val lifecycleOwner = LocalLifecycleOwner.current
                var areBubblesEnabled by remember { mutableStateOf(viewModel.areBubblesAllowed()) }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            areBubblesEnabled = viewModel.areBubblesAllowed()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp)
                            .systemBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Section: Profile
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(140.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                // Online Status Indicator
                                Surface(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .offset(x = (-8).dp, y = (-8).dp),
                                    shape = CircleShape,
                                    color = Color(0xFF4CAF50),
                                    border = androidx.compose.foundation.BorderStroke(4.dp, MaterialTheme.colorScheme.background)
                                ) {}
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = stringResource(R.string.contact_name),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            
                            Text(
                                text = "Available for chat",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Bottom Section: Actions
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !areBubblesEnabled) {
                                Text(
                                    text = stringResource(R.string.bubbles_disabled_message),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Button(
                                    onClick = { viewModel.openSettings() },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(28.dp)
                                ) {
                                    Text(stringResource(R.string.enable_settings_button))
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.sendBubbleNotification() },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(28.dp)
                                ) {
                                    Text(stringResource(R.string.send_message_button))
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Theme Toggle
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Dynamic Colors",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Switch(
                                        checked = isDynamicTheme,
                                        onCheckedChange = { viewModel.toggleDynamicTheme(it) },
                                        thumbContent = if (isDynamicTheme) {
                                            {
                                                Icon(
                                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                                    painter = painterResource(id = R.drawable.ic_stat_bubble),
                                                    contentDescription = null,
                                                )
                                            }
                                        } else null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}