package com.skat.restaurant.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skat.restaurant.ui.components.textFields.AppTextFields
import com.skat.restaurant.ui.theme.Gray
import com.skat.restaurant.ui.theme.Orange700

@Composable
fun RatingBarDialog(
    maxRating: Int = 5,
    onDismissRequest: () -> Unit,
    onRatingSet: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Оценка заказа", style = MaterialTheme.typography.h5) },
        text = {
            Column {
                Text(text = "Пожалуйста, оцените заказ")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..maxRating) {
                        val isSelected = i <= rating

                        Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star $i",
                            tint = if (isSelected) Orange700 else Gray,
                            modifier = Modifier
                                .clickable { rating = i }
                                .padding(4.dp)
                        )

                    }
                }
                AppTextFields(
                    value = message,
                    onValueChange = { message = it },
                    labelText = "Комментарии"
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onRatingSet(rating, message)
                onDismissRequest()
            })
            {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Отмена")
            }
        }
    )

}