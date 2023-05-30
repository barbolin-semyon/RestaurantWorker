package com.skat.restaurant.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tinn.ui.components.textFields.TextFieldPassword
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.ui.components.textFields.TextFieldEmail
import com.skat.restaurant.ui.theme.Orange200
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Orange200)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Авторизация",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextFieldEmail(email = email, onValueChange = { email = it })

                TextFieldPassword(
                    password = password,
                    onValueChange = { password = it },
                    isError = password.length < 8 && password.isNotEmpty(),
                    errorText = "Пароль должен содержать 8 символов",
                    labelText = "Введите пароль"
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 32.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { viewModel.signInWithEmail(email, password) },
                    enabled = email.emailIfValid() && password.length >= 8,
                ) {
                    Text(text = "Войти")
                }
                TextButton(onClick = { navController.navigate(Screens.Registration.route) }) {
                    Text("Нет аккаунта? Зарегистрирутесь!")
                }

            }
        }
    }
}