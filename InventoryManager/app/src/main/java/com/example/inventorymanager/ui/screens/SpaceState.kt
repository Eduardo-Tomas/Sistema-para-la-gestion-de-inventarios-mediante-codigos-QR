package com.example.inventorymanager.ui.screens

import com.example.inventorymanager.model.Space

data class SpaceState (
    val spaces: List<Space> = listOf(),

    val spaceRowClicked: Space = Space(
        0,
        "",
        ""
    ),

    val spaceEdit: Space = Space(
        0,
        "",
        ""
    ),
    val spaceEditName: String = "",

    val spaceNewName: String = "",

    val error: Boolean = false,
    val isLoading: Boolean = false
)