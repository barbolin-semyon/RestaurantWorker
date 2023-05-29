package com.skat.restaurant.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.viewModel.FirestoreViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MenuListView(
    eats: List<Menu>,
    background: @Composable (Menu) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items(eats) {
            ItemMenuView(
                menu = it,
                background = { background(it) }
            )
        }
    }
}

@Composable
fun ItemMenuView(menu: Menu, background: @Composable () -> Unit) {
    SwipeToStartDismissWithCenter(
        background = { background() },
    ) {
        Card(
            Modifier
                .fillMaxWidth(),
            elevation = 8.dp
        ) {

            var isVisibleIngridients by remember { mutableStateOf(false) }

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
                        text = "${menu.name}",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "Стоимость: ${menu.price}",
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = "Длительность (ч): ${menu.time}",
                        color = MaterialTheme.colors.onSurface
                    )

                    AnimatedVisibility(visible = isVisibleIngridients) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colors.primary)
                                .padding(start = 8.dp)
                        ) {
                            menu.ingredients.forEach {
                                Text(text = it, color = Color.White, fontWeight = FontWeight.Bold)
                            }

                        }
                    }

                    TextButton(onClick = { isVisibleIngridients = isVisibleIngridients.not() }) {
                        Text(text = "Посмотреть ингридиенты")
                    }
                }
            }
        }
    }
}