package com.skat.restaurant.ui.navigation

import com.example.restaurant.R

/**
 * Класс, включающий в себя экраны для [AppNavHost]
 */
sealed class Screens(val route: String) {
    object Role : Screens("role")
    object SignIn : Screens("signIn")
    object Registration : Screens("registration")
    object Main : Screens("main")
}

sealed class WorkScreens(val route: String) {
    object MapScreen : WorkScreens("map")
    object Confirmation: WorkScreens("confirm")
}

sealed class BottomScreens(val label: String, val route: String, val icon: Int) {
    object WorkScreen : BottomScreens("Зал", "work", R.drawable.ic_map)
    object ProfileScreen : BottomScreens("Профиль", "profile", R.drawable.ic_profile)

    object CurrentOrders : BottomScreens("Заказы", "current_orders", R.drawable.ic_orders)

    object EatScreen : BottomScreens("Меню", "eat", R.drawable.ic_menu)
    object ReportScreen : BottomScreens("Отчетность", "report", R.drawable.ic_work)


}

val bottomScreensForWaiter = listOf(
    BottomScreens.WorkScreen,
    BottomScreens.CurrentOrders,
    BottomScreens.ProfileScreen
)

val bottomScreensForCook = listOf(
    BottomScreens.EatScreen,
    BottomScreens.CurrentOrders
)

val bottomScreensForAdministrator = listOf(
    BottomScreens.WorkScreen,
    BottomScreens.CurrentOrders,
    BottomScreens.EatScreen,
    BottomScreens.ReportScreen
)