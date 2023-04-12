package com.skat.restaurant.ui.features.auth

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tinn.ui.components.textFields.TextFieldPassword
import com.skat.restaurant.ui.components.textFields.TextFieldEmail
import com.skat.restaurant.ui.components.textFields.TextFieldPhoneNumber
import com.skat.restaurant.ui.components.textFields.TextFieldsWithLabelError
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.utils.emailIfValid
import com.skat.restaurant.viewModel.AuthorizationViewModel

/**
 * Функция отображения экрана регистрации
 */
@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel = viewModel(AuthorizationViewModel::class.java)

    val isAuthorization by viewModel.isAuthorization.collectAsState()
    if (isAuthorization == true) {
        navController.navigate(Screens.Main.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val scrollState = rememberScrollState()

    Column(
        Modifier.fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.h5)

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var firstName by remember { mutableStateOf("") }
        var secondName by remember { mutableStateOf("") }

        TextFieldEmail(email = email, onValueChange = { email = it })

        TextFieldsWithLabelError(
            value = firstName,
            onValueChange = { firstName = it },
            labelText = "Введите имя",
        )

        TextFieldsWithLabelError(
            value = secondName,
            onValueChange = { secondName = it },
            labelText = "Введите профиль",
        )

        TextFieldPhoneNumber(
            value = phone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            onValueChange = { phone = it },
        )

        TextFieldPassword(
            password = password,
            onValueChange = { password = it },
            isError = password.length < 8 && password.isNotEmpty(),
            errorText = "Пароль должен содержать 8 символов",
            labelText = "Введите пароль"
        )

        TextFieldPassword(
            password = repeatPassword,
            onValueChange = { repeatPassword = it },
            isError = password != repeatPassword && password.isNotEmpty(),
            errorText = "Пароли не совпадают",
            labelText = "Повторите пароль"
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { viewModel.register(email, password) },
            enabled = email.emailIfValid() && password.length >= 8 && password == repeatPassword,
            content = { Text("Зарегистрироваться") }
        )
    }
}