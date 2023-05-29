package com.skat.restaurant.model.entities

data class Menu(
    val id: String = "",
    val cook: String = "",
    val img: String = "",
    val ingredients: List<String> = emptyList(),
    val name: String = "",
    val price: Float = 0f,
    val status: Boolean = false,
    val time: Float = 0f
)
