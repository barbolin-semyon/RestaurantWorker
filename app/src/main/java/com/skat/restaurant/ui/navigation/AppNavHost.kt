package com.skat.restaurant.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
 import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skat.restaurant.ui.features.auth.ChooseRoleView
import com.skat.restaurant.ui.features.auth.RegisterScreen
import com.skat.restaurant.ui.features.auth.SignInScreen
import com.skat.restaurant.ui.features.main.MainScreen


/**
 * Первоначальный граф навигации, включающий в себя авторизацию,
 * подтвердение почты, ввод информации и главный экран
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination ,
        modifier = modifier
    ) {
        composable(Screens.Role.route) {
            ChooseRoleView(navController)
        }

        composable(Screens.SignIn.route) {
            SignInScreen(navController)
        }

        composable(Screens.Registration.route) {
            RegisterScreen(navController)
        }

        composable(Screens.Main.route) {
            MainScreen(authNavController = navController)
        }
    }
}

