package com.skat.restaurant.model.entities

import com.google.firebase.firestore.Query
import com.skat.restaurant.model.entities.Menu
import java.util.*

data class History(
    val difference: Float,
    val entTime: Date,
    val startTime: Date,
    val menu: List<Menu>,
    val price: Float,
    val rating: Float,
    val table: Query,
    val waiter: String
)
