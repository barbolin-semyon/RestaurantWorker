package com.skat.restaurant.ui.navigation

/**
 * Класс, включающий в себя экраны для [AppNavHost]
 */
sealed class Screens(val route: String) {
    object SignIn : Screens("signIn")
    object Registration : Screens("registration")
    object Main : Screens("main")
}
