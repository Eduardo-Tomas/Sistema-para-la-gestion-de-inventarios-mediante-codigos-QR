package com.example.inventorymanager.ui.screens

import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.Notification
import com.example.inventorymanager.model.Space

data class NotificationState (
    val notifications: List<Notification> = listOf(),

    val objects: List<ModelObject> = listOf(),

    val spaces: List<Space> = listOf(),

    val notificationTypeSelected: Int = -1,

    val stockLimitSelected: String = "",

    val spaceIDSelected: Int = -1,

    val objectIDSelected: Int = -1,

    val notificationRowSelected: Int = -1,

    val notificationPeriod: String = "",

    // Type of notification to which the interval between notifications will be set
    val notificationPeriodType: Int = -1,

    val error: Boolean = false,
    val isLoading: Boolean = false
)