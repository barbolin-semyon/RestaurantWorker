package com.skat.restaurant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red

private val LightColorPalette = lightColors(
    primary = Blue700,
    onPrimary = Color.White,
    primaryVariant = Orange700,
    secondary = Blue500,
    error = Red,
    background = Orange200,
    onBackground = DarkGray1,
    surface = Color.White,
    onSurface = DarkGray1
)

private val DarkColorPalette = darkColors(
    primary = Orange500,
    onPrimary = Color.White,
    primaryVariant = Orange700,
    secondary = Blue500,
    error = Red,
    background = DarkGray3,
    onBackground = DarkGray2,
    surface = DarkGray2,
    onSurface = Gray
)


@Composable
fun RestaurantTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}