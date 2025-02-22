package com.hannessjolander.calendar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Slate800,  // Moved up two steps
    secondary = Slate700,  // Adjusted accordingly
    tertiary = Slate600,  // Adjusted for consistency
    background = Slate50,  // Background remains unchanged
    surface = Slate200,  // Surface remains unchanged
    onPrimary = Slate50,  // Text/icon color on primary
    onSecondary = Slate50,  // Text/icon color on secondary
    onBackground = Slate900,  // Text on background
    onSurface = Slate500,  // Text on surface
    surfaceContainerHighest = FUCHSIA700
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Slate500,  // Primary color
    secondary = Slate400,  // Secondary color
    tertiary = Slate500,  // Tertiary color
    background = Slate900,  // Background
    surface = Slate800,  // Surface
    onPrimary = Slate900,  // Text/icon color on primary
    onSecondary = Slate950,  // Text/icon color on secondary
    onBackground = Slate200,  // Text on background
    onSurface = Slate200,  // Text on surface
    surfaceContainerHighest = FUCHSIA700
)

@Composable
fun CalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}