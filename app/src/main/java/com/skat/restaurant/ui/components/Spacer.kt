package com.skat.restaurant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skat.restaurant.ui.theme.DarkGray1
import com.skat.restaurant.ui.theme.Gray

/**
 * Горизонтальная линия для отделения контента друг от друга
 * @param[colorSpacer] Цвет линии
 * @param[height] Толщина линии
 * @param[modifier] Модификатор
 */
@Composable
fun HorizontalSpacer(
    colorSpacer: Color = DarkGray1,
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = 1.dp,
) {
    Spacer(
        modifier = modifier
            .height(height)
            .background(colorSpacer)
    )
}