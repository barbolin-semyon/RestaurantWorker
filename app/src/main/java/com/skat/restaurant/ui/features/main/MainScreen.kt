package com.skat.restaurant.ui.features.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.skat.restaurant.ui.navigation.MainBottomNavigation
import com.skat.restaurant.ui.navigation.MainNavHost

@Composable
fun MainScreen(authNavController: NavController) {
    val mainController = rememberNavController()

    Scaffold(bottomBar = { MainBottomNavigation(mainController) }) {
        MainNavHost(navController = mainController, paddingValues = it)
    }
}