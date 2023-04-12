package com.skat.restaurant.model.entities

data class Menu(
    val cook: String,
    val img: String,
    val ingredients: List<String>,
    val name: String,
    val price: Float,
    val status: Boolean,
    val time: Float
)
