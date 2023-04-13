package com.example.tinn.ui.components.spinner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurant.R
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.theme.Alpha
import com.skat.restaurant.ui.theme.Gray

/**
 * Выпадающий список вариантов
 * @param[hint] Текст, отображаемый в закрытом состоянии (заголовок)
 * @param[hintColor] Цвет заголовка
 * @param[textColor] Цвет текста элементов списка
 * @param[borderColor] Цвет рамки списка в закрытом состоянии
 * @param[fontSize] Размер текста
 * @param[contentIcon] Composable функция контента иконки справа
 * @param[modifier] Модификатор
 * @param[content] Контент в открытом состоянии выпадающего списка
 */

@Composable
fun Spinner(
    hint: String,
    hintColor: Color = MaterialTheme.colors.onSurface,
    textColor: Color = MaterialTheme.colors.onSurface,
    borderColor: Color = Color.Gray,
    fontSize: TextUnit = 18.sp,
    contentIcon: @Composable() (() -> Unit)? = null,
    modifier: Modifier = Modifier.padding(top = 16.dp),
    content: @Composable (hide: () -> Unit) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { isExpand = true }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = hint,
                fontSize = fontSize,
                color = hintColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (contentIcon == null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Раскрыть список",
                    tint = textColor
                )
            } else {
                contentIcon()
            }
        }

        DropdownMenu(
            expanded = isExpand,
            onDismissRequest = { isExpand = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            content() { isExpand = false }
        }

        HorizontalSpacer(colorSpacer = borderColor)
    }
}