package com.skat.restaurant.ui.navigation

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Функция отображения нижней панели навигации,
 * работающая с [MainNavHost] и [screensForBottomNavLists]
 */
@Composable
fun MainBottomNavigation(navController: NavController) {
    val state = navController.currentBackStackEntryAsState()
    val destination = state.value?.destination

    destination?.let {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 16.dp
        ) {
            bottomScreens.forEach { currentScreen ->
                val isSelected =
                    destination.hierarchy.any { it.route == currentScreen.route }
                val color =
                    if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

                BottomNavigationItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(currentScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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