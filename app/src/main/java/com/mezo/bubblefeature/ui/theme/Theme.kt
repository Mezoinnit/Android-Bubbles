package com.mezo.bubblefeature.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// --- Black & White Themes ---

private val BWLightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Black, // Sent bubbles
    onPrimaryContainer = White,
    secondary = White,
    onSecondary = Black,
    secondaryContainer = LightGray, // Received bubbles
    onSecondaryContainer = Black,
    tertiary = DarkGray,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black
)

private val BWDarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = White, // Sent bubbles
    onPrimaryContainer = Black,
    secondary = Black,
    onSecondary = White,
    secondaryContainer = DarkGray, // Received bubbles
    onSecondaryContainer = White,
    tertiary = LightGray,
    background = Black,
    surface = Black,
    onBackground = White,
    onSurface = White
)

// --- Fallback/Original Themes ---

private val FallbackDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val FallbackLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun BubbleFeatureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Controlled by User Settings
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    
    val colorScheme = when {
        // Case 1: Dynamic Color Enabled AND Supported (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Case 2: Custom Black & White Theme (Dynamic Disabled)
        !dynamicColor -> {
            if (darkTheme) BWDarkColorScheme else BWLightColorScheme
        }
        // Case 3: Fallback for older devices if dynamic is requested but not supported (rare now)
        darkTheme -> FallbackDarkColorScheme
        else -> FallbackLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}