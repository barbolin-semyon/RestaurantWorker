package com.skat.restaurant.ui.navigation

import androidx.compose.runtime.Composable
 import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skat.restaurant.ui.features.auth.RegisterScreen
import com.skat.restaurant.ui.features.auth.SignInScreen


/**
 * Первоначальный граф навигации, включающий в себя авторизацию,
 * подтвердение почты, ввод информации и главный экран
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    changeTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screens.SignIn.route) {
            SignInScreen(navController)
        }

        composable(Screens.Registration.route) {
            RegisterScreen(navController)
        }

        composable(Screens.Main.route) {

        }
    }
}

