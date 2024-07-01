package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification (
    val notificationID: Int,
    val objectID: Int,
    val objectName: String,
    val spaceID: Int,
    val spaceName: String,
    val notificationTypeID: Int,
    val description: String,
    val limitAmount: Int,
    var isActive: Int
)

@Serializable
data class NotificationC (
    val notificationID: Int,
    val objectID: Int,
    val spaceID: Int,
    val notificationTypeID: Int,
    val limitAmount: Int,
    val isActive: Int
)

@Serializable
data class NotificationActive (
    val isActive: Int
)

@Serializable
data class NotificationStockLimit(
    val limitAmount: Int
)

@Serializable
data class NotificationDTO (
    val objectID: Int,
    val spaceID: Int,
    val notificationTypeID: Int,
    val limitAmount: Int,
    val isActive: Int
)

@Serializable
data class NotificationMessage (
    val message: String
)