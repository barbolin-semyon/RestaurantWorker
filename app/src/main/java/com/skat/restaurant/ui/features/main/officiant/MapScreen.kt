package com.skat.restaurant.ui.features.main.officiant

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.restaurant.R
import com.skat.restaurant.ui.components.BottomSheetView
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(navController: NavController) {
    BottomSheetView(
        sheetContent = { TableInformationView() }
    ) { state, scope ->
        LaunchedEffect(key1 = Unit) { state.show() }
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .zoomable(
                    rememberZoomState()
                )
                .clickable { }
        )
    }
}