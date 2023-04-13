package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skat.restaurant.ui.components.MenuListView
import com.skat.restaurant.viewModel.FirestoreViewModel

@Composable
fun AllMenuScreen(navController: NavController) {
    val viewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = { viewModel.getMenu() })
    Column(Modifier.fillMaxSize().background(Color.White).padding(top = 32.dp)) {
        val items by viewModel.menu.collectAsState()
        if (items.isNotEmpty()) {
            MenuListView(eats = items)
        }

    }
}