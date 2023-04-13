package com.example.tinn.ui.components.spinner

import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Выпадающий список вариантов выбора языка
 * @param[hint] Текст, отображаемый в закрытом состоянии (заголовок)
 * @param[hintColor] Цвет заголовка
 * @param[textColor] Цвет текста элементов списка
 * @param[modifier] Модификатор
 * @param[onClick] Лямбда-функция, вызываемая при выборе одного из элементов
 */
@Composable
fun RoleSpinner(
    hint: String,
    hintColor: Color = MaterialTheme.colors.onSurface,
    textColor: Color = MaterialTheme.colors.onSurface,
    modifier: Modifier = Modifier.padding(start = 32.dp, end= 32.dp, top = 16.dp),
    onClick: (item: String) -> Unit
) {
    Spinner(
        hint = hint.ifEmpty { "Выберите роль" },
        hintColor = hintColor,
        textColor = textColor,
        fontSize = MaterialTheme.typography.body2.fontSize,
        modifier = modifier
    ) { hide ->
        for (item in listLanguage) {
            DropdownMenuItem(onClick = {
                hide()
                onClick(item)
            }) {
                Text(
                    text = item,
                    color = hintColor,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}

private val listLanguage = listOf(
    "Повар",
    "Официант",
    "Администратор"
)
