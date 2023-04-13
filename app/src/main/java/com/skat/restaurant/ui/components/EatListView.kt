package com.skat.restaurant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skat.restaurant.model.entities.Menu
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MenuListView(
    eats: List<Menu>
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 16.dp)) {
        items(eats) {
            ItemMenuView(menu = it)
        }
    }
}

@Composable
fun ItemMenuView(menu: Menu) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            GlideImage(imageModel = menu.img)

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

                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Посмотреть ингридиенты")
                }
            }
        }
    }
}