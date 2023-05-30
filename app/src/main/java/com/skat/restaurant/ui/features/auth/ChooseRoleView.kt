package com.skat.restaurant.ui.features.auth

import android.content.Context.MODE_PRIVATE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.skat.restaurant.ui.components.textFields.AppTextFields
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.ui.theme.Gray
import com.skat.restaurant.ui.theme.Orange200
import com.skat.restaurant.ui.theme.Orange500
import com.skat.restaurant.ui.theme.Orange700
import com.skat.restaurant.utils.gradientBackground
import com.skat.restaurant.viewModel.AuthorizationViewModel
import com.skat.restaurant.viewModel.RequestObserver

@Composable
fun ChooseRoleView(navController: NavHostController) {
    val viewModel: AuthorizationViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val pref = context.getSharedPreferences("settings", MODE_PRIVATE)

    val state by viewModel.role.collectAsState()
    LaunchedEffect(key1 = state, block = {
        if (state.isNotEmpty()) {
            if (state != "error") {
                pref.edit().putString("role", state).apply()
                val route = if (state == "waiter") Screens.SignIn.route else Screens.Main.route
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId)
                }
            }
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground(listOf(Orange500, Orange700), 45f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = 16.dp,
        ) {
            var selectedRole by remember { mutableStateOf("guest") }
            Column(
                Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Выберите роль",
                    style = MaterialTheme.typography.h3,
                    color = Orange700,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ItemRatio(title = "Гость", isSelected = selectedRole == "guest") {
                        selectedRole = "guest"
                    }

                    ItemRatio(title = "Работник", isSelected = selectedRole == "worker") {
                        selectedRole = "worker"
                    }
                }

                var code by remember { mutableStateOf("") }

                AnimatedVisibility(selectedRole == "worker") {
                    AppTextFields(labelText = "Введите код",
                        value = code,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        onValueChange = { code = it })
                }

                Button(
                    onClick = {
                        if (selectedRole == "guest") {
                            pref.edit().putString("role", "guest").apply()
                            navController.navigate(Screens.Main.route) {
                                popUpTo(navController.graph.startDestinationId)
                            }
                        } else {
                            viewModel.getRoleByCode(code)
                        }

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Продолжить")
                }
            }
        }
    }
}

@Composable
fun ItemRatio(title: String, isSelected: Boolean, setSelected: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        RadioButton(
            selected = isSelected,
            onClick = setSelected,
            colors = RadioButtonDefaults.colors(selectedColor = Orange700)
        )

        Text(
            text = title,
            color = if (isSelected) Orange700 else Color.Black,
            style = MaterialTheme.typography.h6
        )

    }
}