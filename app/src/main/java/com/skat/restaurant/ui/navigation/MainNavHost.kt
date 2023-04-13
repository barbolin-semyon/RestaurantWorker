package com.skat.restaurant.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.skat.restaurant.ui.features.main.ProfileScreen
import com.skat.restaurant.ui.features.main.officiant.AllMenuScreen
import com.skat.restaurant.ui.features.main.officiant.MapScreen

@Composable
fun MainNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = BottomScreens.WorkScreen.route) {
        officiant(navController)
        composable(BottomScreens.ProfileScreen.route) {
            ProfileScreen()
        }
    }
}

fun NavGraphBuilder.officiant(navController: NavController) {
    navigation(
        startDestination = WorkScreens.MapScreen.route,
        route = BottomScreens.WorkScreen.route
    ) {
        composable(WorkScreens.MapScreen.route) {
            MapScreen(navController = navController)
        }

        composable(WorkScreens.EatScreen.route) {
            AllMenuScreen(navController)
        }

        composable(WorkScreens.Confirmation.route) {

        }
    }
}