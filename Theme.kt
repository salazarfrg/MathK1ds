package com.ricewood.mathkids.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KidsColorScheme = darkColorScheme(
    primary = Purple,
    secondary = Gold,
    tertiary = Blue,
    background = Bg,
    surface = Card,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun MathKidsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KidsColorScheme,
        typography = MathKidsTypography,
        content = content
    )
}
