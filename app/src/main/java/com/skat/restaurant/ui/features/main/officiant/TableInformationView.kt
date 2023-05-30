package com.skat.restaurant.ui.features.main.officiant

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.model.entities.User
import com.skat.restaurant.model.network.FirebaseAuthDataSource
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.MenuListView
import com.skat.restaurant.ui.components.RatingBarDialog
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.ui.navigation.BottomScreens
import com.skat.restaurant.ui.navigation.WorkScreens
import com.skat.restaurant.utils.parseDateWithTime
import com.skat.restaurant.viewModel.AuthorizationViewModel
import com.skat.restaurant.viewModel.FirestoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun TableInformationView(
    navController: NavController,
    table: Table,
    isVisible: Boolean,
    viewModel: FirestoreViewModel,
) {

    val context = LocalContext.current
    val pref = context.getSharedPreferences("settings", MODE_PRIVATE)
    var currentTime by remember { mutableStateOf(Date().parseDateWithTime()!!) }
    LaunchedEffect(key1 = table, block = {
        launch {
            while (isVisible) {
                delay(1000)
                currentTime = Date().parseDateWithTime()!!
            }
        }
    })

    var isVisibleRating by remember { mutableStateOf(false) }
    val authViewModel: AuthorizationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

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
        if (historyRef != null && table.status) {
            viewModel.getHistory(historyRef!!)
            val current by viewModel.history.collectAsState()
            current?.let { history ->

                HorizontalSpacer(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 32.dp)
                )

                if (isVisibleRating) {
                    RatingBarDialog(
                        onDismissRequest = { isVisibleRating = false },
                        onRatingSet = { rating, message ->
                            pref.edit().putInt("table", -1).apply()

                            viewModel.updateTable(
                                tableId = table.number,
                                values = hashMapOf("status" to false)
                            )

                            viewModel.updateHistory(
                                historyId = history.id, values = hashMapOf(
                                    "rating" to rating,
                                    "entTime" to Date(),
                                    "message" to message
                                )
                            )
                        }
                    )
                }

                if (history.menu.isNotEmpty()) {
                    LaunchedEffect(key1 = Unit, block = {
                        viewModel.getMenu(history.menu)
                    })

                    val values by viewModel.orders.collectAsState()

                    if (values.isNotEmpty()) {
                        MenuListView(
                            eats = values.map { it["menu"] as Menu },
                            role = "cook"
                        ) { currentMenu ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(120.dp),
                                    onClick = {
                                        viewModel.updateHistory(
                                            historyId = history.id,
                                            hashMapOf("menu" to history.menu.filter { (it["menu"] as Menu).id != currentMenu.id })
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                                ) {
                                    Text(text = "Удалить")
                                }
                            }
                        }
                    }
                }

                TextWithTitle("Оффициант", history.waiter)
                TextWithTitle("Начало заказа", history.startTime.parseDateWithTime()!!)

                TextWithTitle(
                    "Окончание заказа",
                    currentTime
                )


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "historyId",
                            history.id
                        )
                        navController.navigate(BottomScreens.EatScreen.route)
                    }) {
                        Text("Заказать блюдо")
                    }

                    Button(onClick = {
                        if (pref.getString("role", "guest") == "guest") {
                            isVisibleRating = true
                        } else {
                            viewModel.updateTable(
                                tableId = table.number,
                                values = hashMapOf(
                                    "status" to false,
                                )
                            )

                            viewModel.updateHistory(
                                historyId = history.id, values = hashMapOf(
                                    "entTime" to Date(),
                                )
                            )
                        }
                    }) {
                        Text("Закрыть заказ")
                    }
                }
            }
        } else {

            Button(onClick = {
                val role  = pref.getString("role", "")

                if (role == "guest") {
                    pref.edit().putInt("table", table.number).apply()
                    viewModel.createHistory(tableId = table.number, waiter = "Клиент")
                }

                if (role == "administrator") {
                    viewModel.createHistory(tableId = table.number, waiter = "Администратор")
                }

                if (role == "waiter") {
                    FirebaseFirestore.getInstance().collection("workers")
                        .document(FirebaseAuthDataSource.getUser()?.uid!!).get()
                        .addOnSuccessListener {
                            val user = it.toObject(User::class.java)
                            viewModel.createHistory(tableId = table.number, waiter = user!!.name)
                        }
                }
            }) {
                Text("Забронировать столик")
            }
        }
    }
}
