package com.example.inventorymanager.ui.screens

import androidx.compose.ui.geometry.Rect
import com.example.inventorymanager.model.DetectionDTO
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.model.TableDetection

data class ScanState (
    // To display the selected or detected spaceQR
    val scannedSpaceQRCode: String? = null,

    // To display a bounding box when detecting an object
    // or space, on the main scanning screen
    val boundingBox: Rect? = null,

    // To know if we detected an object or space
    val objects: List<Int> = listOf(),
    val spaces: List<Int> = listOf(),

    // To display the space list
    val spaceObjects: List<Space> = listOf(),

    // Detections in DB
    val detections: List<TableDetection> = listOf(),

    // Objects found in a space
    val detectedObjects: List<DetectionDTO> = listOf(),

    val currentSpace: Int = -1,

    val errorGetting: Boolean = false,

    val errorInserting: Boolean = false,

    val isLoading: Boolean = false
)