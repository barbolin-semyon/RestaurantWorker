package com.skat.restaurant.ui.features.main.officiant

import android.content.Context.MODE_PRIVATE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skat.restaurant.model.entities.History
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.ui.components.MenuListView
import com.skat.restaurant.viewModel.FirestoreViewModel

@Composable
fun AllMenuScreen(navController: NavController, historyId: String?) {
    val viewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = { viewModel.getMenu() })

    var selectedMenu by remember { mutableStateOf(emptyList<Menu>()) }
    var allPrice by remember { mutableStateOf(0f) }
    val history by viewModel.history.collectAsState()
    LaunchedEffect(key1 = history, block = {
        history?.let {
            viewModel.updateHistory(
                it.id,
                hashMapOf("price" to it.price + allPrice, "menu" to it.menu + selectedMenu.map {
                    hashMapOf(
                        "reference" to viewModel.getQueryMenu(it.id), "status" to "Ожидание"
                    )
                })
            )
            navController.popBackStack()
        }
    })

    val context = LocalContext.current
    val pref = context.getSharedPreferences("settings", MODE_PRIVATE)

    Scaffold(
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            Box(Modifier.size(64.dp)) {
                AnimatedVisibility(visible = selectedMenu.isNotEmpty()) {
                    historyId?.let {
                        FloatingActionButton(onClick = {
                            viewModel.getHistory(historyId)
                        }) {
                            Text(text = selectedMenu.size.toString())
                        }
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
            val role by remember { mutableStateOf(pref.getString("role", "")!!) }
            if (items.isNotEmpty()) {
                MenuListView(eats = items, role = role) {
                    if (role == "cook") {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = if (it.status) Color.Red else MaterialTheme.colors.primary),
                                onClick = {
                                    viewModel.changeStatusMenuItemInStopList(
                                        menuId = it.id,
                                        status = it.status.not()
                                    )
                                }
                            ) {
                                Text(text = if (it.status) "В стоп-лист" else "Восстановить")
                            }
                        }
                    } else if (history != null) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(120.dp),
                                onClick = {
                                    selectedMenu = selectedMenu + it
                                    allPrice = allPrice + it.price
                                }) {
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
}