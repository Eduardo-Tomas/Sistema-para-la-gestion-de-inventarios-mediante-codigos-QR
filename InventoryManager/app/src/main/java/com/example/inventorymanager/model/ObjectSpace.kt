package com.example.inventorymanager.model

import kotlinx.serialization.Serializable

@Serializable
data class ObjectSpace (
    val objectSpaceID: Int,
    val objectID: Int,
    val spaceID: Int
)

@Serializable
data class ObjectSpaceFO (
    val objectID: Int
)

@Serializable
data class  ObjectSpaceFS (
    val spaceID: Int
)