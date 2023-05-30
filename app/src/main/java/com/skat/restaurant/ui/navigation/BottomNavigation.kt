package com.skat.restaurant.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Функция отображения нижней панели навигации,
 * работающая с [MainNavHost] и [screensForBottomNavLists]
 */
@Composable
fun MainBottomNavigation(navController: NavController, role: String) {
    val state = navController.currentBackStackEntryAsState()
    val destination = state.value?.destination

    val screens = when (role) {
        "waiter" -> bottomScreensForWaiter
        "administrator" -> bottomScreensForAdministrator
        else -> bottomScreensForCook
    }

    destination?.let {
        AnimatedVisibility(visible = role != "guest") {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 16.dp
            ) {
                screens.forEach { currentScreen ->


                    val isSelected =
                        destination.hierarchy.any { it.route == currentScreen.route }
                    val color =
                        if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

                    BottomNavigationItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(currentScreen.route) {
                                popUpTo(navController.graph.startDestinationId)
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = currentScreen.icon),
                                contentDescription = currentScreen.label,
                                Modifier.size(24.dp),
                                tint = color
                            )
                        },
                        label = {
                            Text(
                                text = currentScreen.label,
                                color = color
                            )
                        },
                    )
                }
            }
        }
    }
}