package com.skat.restaurant.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

enum class DismissValue {
    /**
     * Indicates the component has not been dismissed yet.
     */
    DismissedToCenter,

    /**
     * Indicates the component has been dismissed in the reverse of the reading direction.
     */
    DismissedToStart
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToStartDismissWithCenter(
    initValue: DismissValue = DismissValue.DismissedToStart,
    swipeRatio: Float = 0.3f,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit,
    content: @Composable () -> Unit,
) = BoxWithConstraints(modifier = modifier) {
    val width = constraints.maxWidth.toFloat()
    val anchors = mutableMapOf(
        -width * swipeRatio to DismissValue.DismissedToCenter,
        0f to DismissValue.DismissedToStart
    )

    val state = rememberSwipeableState(initialValue = initValue)

    Box(
        Modifier.swipeable(
            state = state,
            enabled = enabled,
            anchors = anchors,
            orientation = Orientation.Horizontal,
        )
    ) {
        Row(
            content = { background() },
            modifier = Modifier.matchParentSize()
        )
        Row(
            content = { content() },
            modifier = Modifier.offset { IntOffset(state.offset.value.roundToInt(), 0) }
        )
    }
}