package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelObject (
    val objectID: Int,
    val objectName: String,
    val objectPrice: Float,
    val objectQR: String
)

@Serializable
data class ModelObjectDTO (
    val objectName: String,
    val objectPrice: Float
)