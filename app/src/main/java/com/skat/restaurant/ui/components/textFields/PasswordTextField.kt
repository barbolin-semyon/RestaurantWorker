package com.example.tinn.ui.components.textFields

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.skat.restaurant.ui.components.textFields.TextFieldsWithLabelError
import com.example.restaurant.R
import com.skat.restaurant.ui.theme.DarkGray1
import com.skat.restaurant.ui.theme.Gray

/**
 * Поле для ввода пароля с иконкой скрытия и показа его значения
 * @param[password] Текущий пароль
 * @param[onValueChange] Функция обновления пароля
 * @param[labelText] Подсказка о содержании ввода текста
 * @param[modifier] Модификатор элемента
 * @param[paddingValues] Значение отступов
 * @param[isError] Флаг появления ошибки. При значении true появляется отображение текста об ошибке
 * @param[errorText] Текст сообщения об ошибке
 */
@Composable
fun TextFieldPassword(
    password: String,
    onValueChange: (newValue: String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 32.dp),
    isError: Boolean,
    errorText: String
) {
    var isShowPassword by remember { mutableStateOf(false) }

    TextFieldsWithLabelError(
        value = password,
        onValueChange = { text -> onValueChange(text) },
        modifier = modifier,
        paddingValues = paddingValues,
        rightIcon = {
            IconButton(onClick = { isShowPassword = isShowPassword.not() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = "Показать пароль",
                    tint = if (!isShowPassword) DarkGray1 else MaterialTheme.colors.primary
                )
            }
        },
        labelText = labelText,
        errorText = errorText,
        isError = isError,
        visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
    )
}