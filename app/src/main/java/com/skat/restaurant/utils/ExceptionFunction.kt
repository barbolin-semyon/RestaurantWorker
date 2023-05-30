package com.skat.restaurant.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Проверка строки на валидность email-адреса
 */
fun String.emailIfValid(): Boolean {
    return (this.isEmpty() || this.matches(Regex("\\S*@\\S*[.]\\S*")))
}

fun Date.parseDateWithTime(): String? {
    val format = SimpleDateFormat("HH:mm:ss", Locale("ru"))
    return format.format(this)
}

fun Date.parseDate(): String? {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    return format.format(this)
}

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {

        val angleRad = angle / 180f * Math.PI

        val x = kotlin.math.cos(angleRad).toFloat()
        val y = kotlin.math.sin(angleRad).toFloat()

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height) - exactOffset,
                end = exactOffset
            ),
            size = size
        )
    }
)