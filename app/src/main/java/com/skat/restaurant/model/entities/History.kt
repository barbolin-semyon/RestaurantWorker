package com.skat.restaurant.model.entities

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.skat.restaurant.model.entities.Menu
import java.util.*
import kotlin.collections.HashMap

data class History(
    val id: String = UUID.randomUUID().toString(),
    val difference: Float = 0f,
    val entTime: Date? = null,
    val startTime: Date = Date(),
    val menu: List<HashMap<String, Any>> = emptyList(),
    val price: Float = 0f,
    val rating: Float = 0f,
    val table: DocumentReference? = null,
    val waiter: String = ""
)
