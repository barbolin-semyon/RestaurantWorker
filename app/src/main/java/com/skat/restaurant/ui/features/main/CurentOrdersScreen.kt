package com.skat.restaurant.ui.features.main

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.DocumentReference
import com.skat.restaurant.model.entities.History
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.ui.components.HorizontalSpacer
import com.skat.restaurant.ui.components.TextWithTitle
import com.skat.restaurant.ui.theme.*
import com.skat.restaurant.viewModel.FirestoreViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlin.collections.HashMap

@Composable
fun ItemHistoryForOrder(
    number: Int,
    ref: DocumentReference,
    viewModel: FirestoreViewModel,
    role: String
) {
    Text(
        text = "Столик ${number}",
        modifier = Modifier.padding(top = 16.dp),
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.h6
    )

    var current by remember { mutableStateOf<History?>(null) }
    var temp by remember { mutableStateOf(mutableListOf<HashMap<String, Any>>()) }
    ref.addSnapshotListener { value, error ->
        temp = mutableListOf()
        current = value?.toObject(History::class.java)
    }

    current?.let { history ->

        HorizontalSpacer(
            modifier = Modifier
                .width(100.dp)
                .padding(bottom = 8.dp)
        )

        var values by remember { mutableStateOf<List<HashMap<String, Any>>>(emptyList()) }
        if (history.menu.isNotEmpty()) {
            history.menu.forEach {
                val reference = it["reference"] as DocumentReference
                reference.get().addOnSuccessListener { doc ->
                    doc.toObject(Menu::class.java)
                        ?.let { it1 ->
                            temp.add(hashMapOf("menu" to it1, "status" to it["status"]!!))
                            if (temp.size == history.menu.size) {
                                values = temp
                            }
                        }
                }
            }

            if (values.isNotEmpty()) {
                values.forEachIndexed { index, hashMap ->
                    ItemOrder(hashMap, role) {
                        viewModel.updateStatusOrder(
                            historyId = history.id,
                            role = role,
                            menu = history.menu,
                            menuIndex = index,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentOrdersScreen() {
    val viewModel: FirestoreViewModel = viewModel()
    DisposableEffect(key1 = Unit) {
        viewModel.enableListenerTables()

        onDispose { viewModel.disableListenerCollectionPlaces() }
    }
    val tables by viewModel.tables.collectAsState()

    val context = LocalContext.current
    val pref = context.getSharedPreferences("settings", MODE_PRIVATE)
    val role by remember {
        mutableStateOf(pref.getString("role", "")!!)
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        items(tables) {
            if (it.current != null && it.status) {
                ItemHistoryForOrder(
                    number = it.number,
                    ref = it.current,
                    viewModel = viewModel,
                    role = role
                )
            }
        }
    }
}

@Composable
fun ItemOrder(
    order: HashMap<String, Any>,
    role: String,
    updateOrder: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 16.dp
    ) {
        val menu = order["menu"] as Menu
        val status = order["status"] as String

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            GlideImage(
                imageModel = menu.img,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(100.dp)
            )

            Column {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface
                )

                TextWithTitle(
                    title = "Статус",
                    value = status,
                    color = when (status) {
                        "На выдаче" -> Green700
                        "Ожидание" -> Orange700
                        else -> DarkGray2
                    }
                )

                val isCook by remember {
                    mutableStateOf(role == "cook")
                }

                if (isCook) {
                    if (status == "Ожидание") {
                        Button(onClick = updateOrder) {
                            Text(text = "На выдаче")
                        }
                    }
                } else if (status == "На выдаче") {
                    Button(onClick = updateOrder) {
                        Text(text = "Выдано")
                    }
                }
            }
        }
    }
}
