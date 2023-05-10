package com.skat.restaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skat.restaurant.ui.navigation.AppNavHost
import com.skat.restaurant.ui.navigation.Screens
import com.skat.restaurant.ui.theme.RestaurantTheme
import com.skat.restaurant.viewModel.AuthorizationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AuthorizationViewModel = viewModel()
            val isAuthorization by viewModel.isAuthorization.collectAsState()
            rememberSystemUiController().setStatusBarColor(Color.White)
            LaunchedEffect(key1 = Unit, block = { viewModel.checkAuthorization() })
            val navController = rememberNavController()
            RestaurantTheme {
                isAuthorization?.let {
                    val startDestination = if (it) Screens.Main.route else Screens.SignIn.route

                    AppNavHost(navController = navController, startDestination = startDestination) {

                    }
                }
            }
        }
    }
}
