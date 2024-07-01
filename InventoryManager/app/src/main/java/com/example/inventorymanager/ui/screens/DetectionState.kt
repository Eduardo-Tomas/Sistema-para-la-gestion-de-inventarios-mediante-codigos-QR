package com.example.inventorymanager.ui.screens

import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.ObjectSpace
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.model.TableDetection

data class DetectionState (
    val spaces: List<Space> = listOf(),

    val spaceRowClicked: Space = Space(0, "", ""),

    val objectsSpacesBySpace: List<ObjectSpace> = listOf(),

    val lastDetectionsBySpace: List<TableDetection> = listOf(),

    // Date of the last detections in the selected space (spaceRowClicked)
    val lastDetectionsDate: String = "",

    val missingObjectsBySpace: List<ModelObject> = listOf(),

    val objectClicked: ModelObject = ModelObject(0, "", 0.0f, ""),

    // All objects contained in the DB
    val databaseObjects: List<ModelObject> = listOf(),

    val error: Boolean = false,
    val isLoading: Boolean = false
)