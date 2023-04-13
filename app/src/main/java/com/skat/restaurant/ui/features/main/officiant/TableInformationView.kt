package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.utils.parseDateWithTime
import com.skat.restaurant.viewModel.FirestoreViewModel

@Composable
fun TableInformationView() {
    val viewModel: FirestoreViewModel = viewModel()
    DisposableEffect(key1 = Unit) {
        viewModel.enableListenerTables()

        onDispose { viewModel.disableListenerCollectionPlaces() }
    }
    val tables by viewModel.tables.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 500.dp)
    ) {
        if (tables.isNotEmpty()) {
            tables[1].current?.let {
                viewModel.getHistory(it)
                val current by viewModel.history.collectAsState()
                current?.let { history ->
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Cтолик ${tables[1].number}",
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        HorizontalSpacer(modifier = Modifier
                            .width(100.dp)
                            .padding(bottom = 32.dp))

                        TextWithTitle("Оффициант", history.waiter)
                        TextWithTitle("Начало заказа", history.startTime.parseDateWithTime()!!)


                    }
                }
            }
        }
    }
}