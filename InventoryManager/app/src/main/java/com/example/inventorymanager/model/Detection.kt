package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Detection (
    val detectionID: Int,
    val objectID: Int,
    val spaceID: Int,
    val captureDate: String,
    val captureTime: String,
)

@Serializable
data class DetectionDTO (
    val objectID: Int,
    var spaceID: Int,
    val captureDate: String,
    val captureTime: String
)

@Serializable
data class TableDetection (
    val detectionID: Int,
    val captureDate: String,
    val captureTime: String,
    val objectID: Int,
    val objectName: String,
    val objectPrice: Float,
    val objectQR: String,
    val spaceID: Int,
    val spaceName: String,
    val spaceQR: String
)