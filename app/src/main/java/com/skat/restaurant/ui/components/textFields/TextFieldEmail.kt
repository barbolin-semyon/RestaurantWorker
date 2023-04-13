package com.skat.restaurant.ui.components.textFields

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skat.restaurant.ui.components.textFields.TextFieldsWithLabelError
import com.skat.restaurant.utils.emailIfValid

/**
 * Ввод адреса почты
 * @param[email] Текущее значение вводимого текста
 * @param[onValueChange] Функция обновления текста
 * "@param[paddingValues] Значение отступов
 */
@Composable
fun TextFieldEmail(
    email: String,
    onValueChange: (newValue: String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = 32.dp),
) {
    TextFieldsWithLabelError(
        value = email,
        onValueChange = { text -> onValueChange(text) },
        paddingValues = paddingValues,
        labelText = "Email",
        isError = email.emailIfValid().not(),
        errorText = "Не валидный email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}