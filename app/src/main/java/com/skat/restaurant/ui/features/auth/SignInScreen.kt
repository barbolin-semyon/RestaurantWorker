package com.skat.restaurant.ui.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tinn.ui.components.textFields.TextFieldPassword
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.ui.components.textFields.TextFieldEmail
import com.skat.restaurant.utils.emailIfValid
import com.skat.restaurant.viewModel.AuthorizationViewModel

/**
 * Функция отображения экрана авторизации
 */
@Composable
fun SignInScreen(navController: NavHostController) {
    val viewModel = viewModel(AuthorizationViewModel::class.java)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRememberUser by remember { mutableStateOf(false) }

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

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Авторизация", style = MaterialTheme.typography.h5)

        TextFieldEmail(email = email, onValueChange = { email = it })

        TextFieldPassword(
            password = password,
            onValueChange = { password = it },
            isError = password.length < 8 && password.isNotEmpty(),
            errorText = "Введите пароль",
            labelText = "Пароль должен содержать 8 символов"
        )


        Button(
            onClick = { viewModel.signInWithEmail(email, password) },
            enabled = email.emailIfValid() && password.length >= 8,
        ) {
            Text(text = "Войти")
        }

        TextButton(onClick = { navController.navigate(Screens.Registration.route) }) {
            Text("Нет аккаунта? Зарегистрируйтесь")
        }
    }
}