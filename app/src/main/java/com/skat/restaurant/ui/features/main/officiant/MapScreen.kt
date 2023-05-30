package com.skat.restaurant.ui.features.main.officiant

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.restaurant.R
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.ui.components.BottomSheetView
import com.skat.restaurant.viewModel.FirestoreViewModel
import io.grpc.Context
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(navController: NavController) {
    val viewModel: FirestoreViewModel = viewModel()
    DisposableEffect(key1 = Unit) {
        viewModel.enableListenerTables()

        onDispose { viewModel.disableListenerCollectionPlaces() }
    }

    val context = LocalContext.current
    val pref = context.getSharedPreferences("settings", ComponentActivity.MODE_PRIVATE)
    val role = pref.getString("role", "")

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var selectedTable by remember { mutableStateOf(Table()) }
    BottomSheetView(
        state = state,
        sheetContent = {
            TableInformationView(
                navController = navController,
                table = selectedTable,
                isVisible = selectedTable.number > -1,
                viewModel = viewModel
            )
        }
    ) { scope ->
        val zoomState = rememberZoomState()
        val tables by viewModel.tables.collectAsState()
        LaunchedEffect(key1 = tables, block = {
            tables.find { selectedTable.number == it.number }?.let {
                selectedTable = it
            }
        })
        var isEditMode by remember { mutableStateOf(false) }

        Scaffold(topBar = {
            TopAppBar(backgroundColor = Color.White) {
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("кухня", style = MaterialTheme.typography.h4)
                    IconButton(
                        onClick = {
                            if (isEditMode) scope.launch {
                                tables.forEach {
                                    viewModel.updateTable(
                                        it.number,
                                        hashMapOf("cordinates" to it.cordinates)
                                    )
                                }
                            }

                            isEditMode = isEditMode.not()
                        }) {
                        if (role == "administrator") {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                tint = if (isEditMode) MaterialTheme.colors.primary else Color.Gray,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }) {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .zoomable(zoomState)
                //.clickable { scope.launch { state.show() } }
            ) {
                tables.forEachIndexed { index, table ->
                    var x by remember { mutableStateOf(table.cordinates[0]) }
                    var y by remember { mutableStateOf(table.cordinates[1]) }
                    LaunchedEffect(key1 = x, key2 = y, block = {
                        tables[index].cordinates = listOf(x, y)
                    })
                    val context = LocalContext.current
                    Card(
                        Modifier
                            .size(80.dp)
                            .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    if (isEditMode) {
                                        x += dragAmount.x
                                        y += dragAmount.y
                                    }
                                }
                            }
                            .background(Color.White)
                            .shadow(16.dp)
                            .clickable {
                                val pref = context.getSharedPreferences(
                                    "settings",
                                    android.content.Context.MODE_PRIVATE
                                )
                                val role = pref.getString("role", "guest")
                                if ((role == "guest" && pref.getInt(
                                        "table",
                                        -1
                                    ) in listOf(table.number, -1)) || role != "guest"
                                ) {
                                    selectedTable = table
                                    scope.launch { state.show() }
                                }
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(table.number.toString(), style = MaterialTheme.typography.h5)
                        }
                    }
                }
            }
        }

    }
}