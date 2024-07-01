package com.example.inventorymanager.ui.screens

import com.example.inventorymanager.model.ModelObject

data class ObjectState (
    val objects: List<ModelObject> = listOf(),

    val objectRowClicked: ModelObject = ModelObject(
        0,
        "",
        0.0F,
        ""
    ),

    val objectEdit: ModelObject = ModelObject(
        0,
        "",
        0.0F,
        ""
    ),
    val objectEditName: String = "",
    val objectEditPrice: String = "",

    val objectNewName: String = "",
    val objectNewPrice: String = "",

    /*val oneObjectCaptured: String = "",*/

    val error: Boolean = false,
    val isLoading: Boolean = false
)