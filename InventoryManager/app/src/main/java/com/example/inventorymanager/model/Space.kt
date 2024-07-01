package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Space (
    val spaceID: Int,
    val spaceName: String,
    val spaceQR: String
)

@Serializable
data class SpaceDTO (
    val spaceName: String
)

// To select an space to generate reports
data class SpaceCheck(
    val spaceID: Int,
    val spaceName: String,
    val isSelected: Boolean
)