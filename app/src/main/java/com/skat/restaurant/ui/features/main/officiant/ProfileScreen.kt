package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skat.restaurant.ui.navigation.BottomScreens
import com.skat.restaurant.viewModel.AuthorizationViewModel
import com.skat.restaurant.viewModel.FirestoreViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.example.restaurant.R
import com.google.firebase.firestore.DocumentReference
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.ui.theme.Orange700
import com.skat.restaurant.utils.parseDate
import com.skat.restaurant.utils.parseDateWithTime

@Composable
fun ProfileScreen() {
    val userViewModel: AuthorizationViewModel = viewModel()
    val dbViewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = {
        userViewModel.getUser()
    })

    val user by userViewModel.user.collectAsState()
    LaunchedEffect(key1 = user, block = {
        user?.let { dbViewModel.getHistoryForUser(it.name) }
    })
    val history by dbViewModel.listHistory.collectAsState()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            Card(elevation = 16.dp, modifier = Modifier.padding(bottom = 16.dp)) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    imageModel = "https://firebasestorage.googleapis.com/v0/b/restaurant-46613.appspot.com/o/image%204.png?alt=media&token=c9e6d7b3-3589-4a3c-bdb3-c68c2c3dee16&_gl=1*3anynt*_ga*MTM3MzY5OTY3Ni4xNjc3MzEzNTQz*_ga_CW55HF8NVT*MTY4NTQyMDAxOS4xMi4xLjE2ODU0MjA4MjMuMC4wLjA."
                )
            }
        }

        item {
            user?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )

                    Column {
                        Text(text = it.name, style = MaterialTheme.typography.h6)
                        Text(text = it.phone)
                        Text(text = it.email)
                    }
                }
            }
        }


        if (history.isNotEmpty()) {
            items(history) {
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