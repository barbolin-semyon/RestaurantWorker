package com.skat.restaurant.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skat.restaurant.ui.features.main.CurrentOrdersScreen
import com.skat.restaurant.ui.features.main.ReportScreen
import com.skat.restaurant.ui.features.main.officiant.AllMenuScreen
import com.skat.restaurant.ui.features.main.officiant.MapScreen
import com.skat.restaurant.ui.features.main.officiant.ProfileScreen

@Composable
fun MainNavHost(navController: NavHostController, paddingValues: PaddingValues, startDestination: String) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = startDestination
    ) {
        officiant(navController)
        composable(BottomScreens.ProfileScreen.route) {
            ProfileScreen()
        }
        composable(BottomScreens.CurrentOrders.route) {
            CurrentOrdersScreen()
        }

        composable(BottomScreens.ReportScreen.route) {
            ReportScreen()
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

        composable(BottomScreens.EatScreen.route) {
            val historyId =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("historyId")

            AllMenuScreen(navController, historyId)
        }

        composable(WorkScreens.Confirmation.route) {

        }
    }
}