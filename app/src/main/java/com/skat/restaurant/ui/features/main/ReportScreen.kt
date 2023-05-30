package com.skat.restaurant.ui.features.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skat.restaurant.ui.navigation.BottomScreens
import com.skat.restaurant.ui.theme.Alpha
import com.skat.restaurant.viewModel.FirestoreViewModel
import com.example.restaurant.R
import com.google.firebase.firestore.DocumentReference
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.ui.theme.Gray
import com.skat.restaurant.ui.theme.Orange700
import com.skat.restaurant.utils.parseDate
import com.skat.restaurant.utils.parseDateWithTime
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun ReportScreen() {
    val viewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getHistory()
    })

    val listHistory by viewModel.listHistory.collectAsState()
    if (listHistory.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                var isExpanded by remember { mutableStateOf(false) }
                var currentKeySort by remember { mutableStateOf(keys[0]) }

                TextButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = { isExpanded = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onSurface,
                        backgroundColor = Alpha
                    )
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = "sort"
                        )
                        Text(text = "Отсортировать")
                    }

                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {


                        keys.forEach {
                            DropdownMenuItem(
                                modifier = Modifier.background(
                                    if (currentKeySort == it) MaterialTheme.colors.background
                                    else MaterialTheme.colors.surface
                                ),
                                onClick = {
                                    viewModel.getHistory(
                                        keySort = it.id,
                                        isReverse = it.reverse
                                    )
                                    currentKeySort = it
                                    isExpanded = false
                                }) {
                                Text(text = it.label)
                            }
                        }
                    }
                }
            }

            items(listHistory) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextWithTitle(title = "Дата", value = it.startTime.parseDate()!!)
                    TextWithTitle(title = "Стоимость", value = "${it.price}р")
                    var isExpanded by remember { mutableStateOf(false) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Orange700,
                            modifier = Modifier
                                .padding(4.dp)
                        )

                        Text(
                            text = it.rating.toString(),
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        TextButton(onClick = { isExpanded = isExpanded.not() }) {
                            Text(text = "Подробнее")
                        }
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            TextWithTitle(
                                title = "Начало",
                                value = it.startTime.parseDateWithTime()!!
                            )
                            TextWithTitle(
                                title = "Конец",
                                value = it.entTime?.parseDateWithTime() ?: ""
                            )
                            TextWithTitle(title = "Официант", value = it.waiter)
                            MenuListForReport(it.menu)
                        }
                    }

                    HorizontalSpacer()
                }
            }
        }
    }
}

@Composable
fun MenuListForReport(menu: List<HashMap<String, Any>>) {
    var temp by remember { mutableStateOf(mutableListOf<HashMap<String, Any>>()) }
    var values by remember { mutableStateOf<List<HashMap<String, Any>>>(emptyList()) }
    menu.forEach {
        val reference = it["reference"] as DocumentReference
        reference.get().addOnSuccessListener { doc ->
            doc.toObject(Menu::class.java)
                ?.let { it1 ->
                    temp.add(hashMapOf("menu" to it1, "status" to it["status"]!!))
                    if (temp.size == menu.size) {
                        values = temp
                    }
                }
        }
    }

    if (values.isNotEmpty()) {
        values.forEach {
            val item = it["menu"]!! as Menu
            Card {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    GlideImage(
                        imageModel = item.img,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .size(100.dp)
                    )

                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

data class Key(
    val id: String,
    val label: String,
    val reverse: Boolean = true,
)

val keys = listOf(
    Key("startTime", "Дата (по возрастанию)"),
    Key("startTime", "Дата (по убыванию)", false),
    Key("rating", "Рейтинг (по возрастанию)"),
    Key("rating", "Рейтинг (по убыванию)", false),
)