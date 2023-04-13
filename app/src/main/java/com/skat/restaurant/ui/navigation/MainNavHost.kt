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

@Composable
fun MainNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = BottomScreens.WorkScreen.route) {
        officiant(navController)
        composable(BottomScreens.ProfileScreen.route) {

        }
    }
}

fun NavGraphBuilder.officiant(navController: NavController) {
    navigation(
        startDestination = WorkScreens.MapScreen.route,
        route = BottomScreens.WorkScreen.route
    ) {
        composable(WorkScreens.MapScreen.route) {

        }

        composable(WorkScreens.EatScreen.route) {

        }

        composable(WorkScreens.Confirmation.route) {

        }
    }
}