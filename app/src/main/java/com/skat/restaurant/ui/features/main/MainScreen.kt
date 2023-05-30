package com.skat.restaurant.ui.features.main

import android.content.Context.MODE_PRIVATE
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.skat.restaurant.ui.features.main.officiant.MapScreen
import com.skat.restaurant.ui.navigation.BottomScreens
import com.skat.restaurant.ui.navigation.MainBottomNavigation
import com.skat.restaurant.ui.navigation.MainNavHost
import com.skat.restaurant.ui.navigation.WorkScreens

@Composable
fun MainScreen(authNavController: NavController) {

    val context = LocalContext.current
    val pref = context.getSharedPreferences("settings", MODE_PRIVATE)
    val mainController = rememberNavController()
    val role by remember { mutableStateOf(pref.getString("role", "guest")!!)}

    Scaffold(bottomBar = {
        MainBottomNavigation(
            mainController,
            role
        )
    }) {
        val startDestination by remember { mutableStateOf(
            when(role) {
                "cook" -> BottomScreens.CurrentOrders.route
                else -> BottomScreens.WorkScreen.route
            }
        ) }
        MainNavHost(navController = mainController, paddingValues = it, startDestination = startDestination)
    }
}