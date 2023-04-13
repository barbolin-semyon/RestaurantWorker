package com.skat.restaurant.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetView(
    sheetContent: @Composable () -> Unit,
    content: @Composable (state: ModalBottomSheetState, scope: CoroutineScope) -> Unit,
) {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.White,
        sheetElevation = 12.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = state,
        sheetContent = { sheetContent() }
    ) {
        content(state, scope)
    }
}