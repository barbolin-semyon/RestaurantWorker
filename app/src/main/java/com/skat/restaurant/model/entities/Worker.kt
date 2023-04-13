package com.skat.restaurant.model.entities

import java.util.UUID

data class Worker(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val phone: String = "",
)
