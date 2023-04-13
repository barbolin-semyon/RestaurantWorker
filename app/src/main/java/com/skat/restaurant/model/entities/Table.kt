package com.skat.restaurant.model.entities

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

data class Table(
    val number: Int = -1,
    val numberOfSeats: Int = 0,
    val status: Boolean = false,
    val current: DocumentReference? = null
)