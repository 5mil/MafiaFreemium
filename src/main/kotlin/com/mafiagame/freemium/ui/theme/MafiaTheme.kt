package com.mafiagame.freemium.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Underworld color palette
val BloodRed = Color(0xFF8B0000)
val DarkAlley = Color(0xFF121212)
val SmokyGray = Color(0xFF1E1E1E)
val GoldAccent = Color(0xFFD4AF37)
val DiamondBlue = Color(0xFF4FC3F7)
val CashGreen = Color(0xFF66BB6A)
val NightBlack = Color(0xFF0A0A0A)
val SoftWhite = Color(0xFFE0E0E0)

private val DarkColorScheme = darkColorScheme(
    primary = BloodRed,
    onPrimary = SoftWhite,
    secondary = GoldAccent,
    onSecondary = NightBlack,
    tertiary = DiamondBlue,
    background = DarkAlley,
    onBackground = SoftWhite,
    surface = SmokyGray,
    onSurface = SoftWhite,
    error = Color(0xFFCF6679)
)

@Composable
fun MafiaTheme(
    darkTheme: Boolean = true, // Always dark for the underworld feel
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}