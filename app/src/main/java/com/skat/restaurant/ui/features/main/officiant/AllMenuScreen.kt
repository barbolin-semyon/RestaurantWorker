package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skat.restaurant.model.entities.History
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.ui.components.MenuListView
import com.skat.restaurant.viewModel.FirestoreViewModel

@Composable
fun AllMenuScreen(navController: NavController, historyId: String) {
    val viewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = { viewModel.getMenu() })

    var selectedMenu by remember { mutableStateOf(emptyList<Menu>()) }
    val history by viewModel.history.collectAsState()
    LaunchedEffect(key1 = history, block = {
        history?.let {
            viewModel.updateHistory(it.id, hashMapOf("menu" to it.menu + selectedMenu.map { viewModel.getQueryMenu(it.id) }))
            navController.popBackStack()
        }
    })

    Scaffold(
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            Box(Modifier.size(64.dp)) {
                AnimatedVisibility(visible = selectedMenu.isNotEmpty()) {
                    FloatingActionButton(onClick = {
                        viewModel.getHistory(historyId)
                    }) {
                        Text(text = selectedMenu.size.toString())
                    }
                }
            }
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(top = 32.dp)
        ) {
            val items by viewModel.menu.collectAsState()
            if (items.isNotEmpty()) {
                MenuListView(eats = items) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(120.dp),
                            onClick = { selectedMenu = selectedMenu + it }) {
                            Icon(
                                painter = painterResource(id = com.example.restaurant.R.drawable.ic_add),
                                modifier = Modifier.size(32.dp),
                                contentDescription = "add"
                            )
                        }
                    }
                }
            }
        }
    }
}