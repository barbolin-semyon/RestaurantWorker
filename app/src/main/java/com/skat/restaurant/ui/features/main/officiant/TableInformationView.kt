package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.MenuListView
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.ui.navigation.WorkScreens
import com.skat.restaurant.utils.parseDateWithTime
import com.skat.restaurant.viewModel.FirestoreViewModel

@Composable
fun TableInformationView(
    navController: NavController,
    table: Table,
    viewModel: FirestoreViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 500.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Cтолик ${table.number}",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .fillMaxWidth()
        )

        val historyRef = table.current
        if (historyRef != null) {
            viewModel.getHistory(historyRef!!)
            val current by viewModel.history.collectAsState()
            current?.let { history ->

                HorizontalSpacer(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 32.dp)
                )

                if (history.menu.isNotEmpty()) {
                    LaunchedEffect(key1 = Unit, block = {
                        viewModel.getMenu(history.menu)
                    })
                    val values by viewModel.menu.collectAsState()

                    if (values.isNotEmpty()) {
                        MenuListView(eats = values)
                    }
                }

                TextWithTitle("Оффициант", history.waiter)
                TextWithTitle("Начало заказа", history.startTime.parseDateWithTime()!!)
                TextWithTitle("Окончание заказа", history.entTime.parseDateWithTime()!!)


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        navController.navigate(WorkScreens.EatScreen.route)
                    }) {
                        Text("Заказать блюдо")
                    }

                    Button(onClick = { /*TODO*/ }) {
                        Text("Закрыть заказ")
                    }
                }

            }
        } else {
            Button(onClick = { /*TODO*/ }) {
                Text("Занять столик")
            }
        }
    }
}
