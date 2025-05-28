package ru.blackmirrror.style.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = bgPrimaryDark,
    onBackground = txPrimaryDark,
    primaryContainer = bgPrimaryContainerDark,
    onPrimaryContainer = txSecondaryDark,

    primary = accentLight,
    onPrimary = accentLight,
    error = error,
    outline = stars
)

private val LightColorScheme = lightColorScheme(
    background = bgPrimaryLight,
    onBackground = txPrimaryLight,
    primaryContainer = bgPrimaryContainerLight,
    onPrimaryContainer = txSecondaryLight,

    primary = accent,
    onPrimary = accentLight,
    error = error,
    outline = stars
)

@Composable
fun TravelerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
