package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class PDF (
    val startDate: String,
    val endDate: String,
    val selectedSpaces: MutableList<Int>
)