package com.skat.restaurant.ui.components.textFields

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.skat.restaurant.utils.CustomVisualTransformation

/**
 * Ввод номера телефона с маской +7-000-000-00-00
 * @param[value] Текущее значение вводимого текста
 * @param[modifier] Модификатор
 * "@param[paddingValues] Значение отступов
 * "@param[onValueChange] Функция обновления текста
 */
@Composable
fun TextFieldPhoneNumber(
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp),
    onValueChange: (newValue: String) -> Unit
) {
    TextFieldsWithLabelError(
        value = value,
        modifier = modifier,
        paddingValues = paddingValues,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        onValueChange = {
            if ((value.length < 10 && it.isDigitsOnly()) || value.length >= it.length) {
                onValueChange(it)
            }
        },
        visualTransformation = CustomVisualTransformation("+7-000-000-00-00", '0'),
        labelText = "Введите номер телефона",
    )
}
