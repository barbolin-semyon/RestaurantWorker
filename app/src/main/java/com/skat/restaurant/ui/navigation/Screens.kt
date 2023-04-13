package com.skat.restaurant.ui.navigation

import com.example.restaurant.R

/**
 * Класс, включающий в себя экраны для [AppNavHost]
 */
sealed class Screens(val route: String) {
    object SignIn : Screens("signIn")
    object Registration : Screens("registration")
    object Main : Screens("main")
}

sealed class WorkScreens(val route: String) {
    object MapScreen : WorkScreens("map")
    object EatScreen : WorkScreens("eat")
    object Confirmation: WorkScreens("confirm")
}

sealed class BottomScreens(val label: String, val route: String, val icon: Int) {
    object WorkScreen : BottomScreens("Работа", "work", R.drawable.ic_work)
    object ProfileScreen : BottomScreens("Профиль", "profile", R.drawable.ic_profile)
}

val bottomScreens = listOf(
    BottomScreens.WorkScreen,
    BottomScreens.ProfileScreen
)