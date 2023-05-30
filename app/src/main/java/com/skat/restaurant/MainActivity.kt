package com.skat.restaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skat.restaurant.ui.navigation.AppNavHost
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.ui.theme.RestaurantTheme
import com.skat.restaurant.viewModel.AuthorizationViewModel
import com.skat.restaurant.viewModel.RequestObserver
import com.skat.restaurant.viewModel.StatusType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AuthorizationViewModel = viewModel()
            rememberSystemUiController().setStatusBarColor(Color.White)
            val navController = rememberNavController()
            val snackBarState = remember { SnackbarHostState() }

            val context = LocalContext.current
            val pref = context.getSharedPreferences("settings", MODE_PRIVATE)
            val role = pref.getString("role", "")

            var startDestination by remember {
                mutableStateOf<String>(
                    if (role?.isNotEmpty() == true) {
                        if (role == "waiter") {
                            viewModel.checkAuthorization()
                            ""
                        } else {
                            Screens.Main.route
                        }
                    } else {
                        Screens.Role.route
                    }
                )
            }

            val authorizationState by viewModel.isAuthorization.collectAsState()
            LaunchedEffect(key1 = authorizationState, block = {
                authorizationState?.let {
                    startDestination = if (authorizationState == true) Screens.Main.route
                    else Screens.SignIn.route
                }
            })

            val errorState by RequestObserver.requestStatus.collectAsState()
            LaunchedEffect(key1 = errorState, block = {
                if (errorState.statusType == StatusType.ERROR) {
                    snackBarState.showSnackbar(errorState.message)
                }
            })

            RestaurantTheme {
                if (startDestination.isNotEmpty()) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackBarState) }
                    ) {
                        AppNavHost(
                            navController = navController,
                            padding = it,
                            startDestination = startDestination,
                        )
                    }
                }
            }
        }
    }
}
