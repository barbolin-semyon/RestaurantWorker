package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.ui.components.BottomSheetView
import com.skat.restaurant.viewModel.FirestoreViewModel
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
    var selectedTable by remember { mutableStateOf(Table()) }
    BottomSheetView(
        sheetContent = {
            TableInformationView(
                navController = navController,
                table = selectedTable,
                viewModel = viewModel
            )
        }
    ) { state, scope ->
        val zoomState = rememberZoomState()
        val tables by viewModel.tables.collectAsState()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .zoomable(zoomState)
            //.clickable { scope.launch { state.show() } }
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 16.dp),
                elevation = 16.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("кухня", style = MaterialTheme.typography.h4)
                }
            }

            tables.forEach {
                var x by remember { mutableStateOf(it.cordinates[0]) }
                var y by remember { mutableStateOf(it.cordinates[1]) }
                Card(
                    Modifier
                        .size(80.dp)
                        .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                x += dragAmount.x
                                y += dragAmount.y
                            }
                        }
                        .background(Color.White)
                        .shadow(16.dp)
                        .clickable {
                            selectedTable = it
                            scope.launch { state.show() }
                        }
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(it.number.toString(), style = MaterialTheme.typography.h5)
                    }
                }
            }
        }
    }
}