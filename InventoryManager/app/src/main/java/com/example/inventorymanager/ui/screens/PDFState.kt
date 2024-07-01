package com.example.inventorymanager.ui.screens

import android.net.Uri
import com.example.inventorymanager.model.Space
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState

data class PDFState (
    val spaces: List<Space> = listOf(),

    // 1 spaces report, 2 detailed report
    val reportType: Int = -1,
    val reportName: String = "",

    val startDate: String = "",
    val endDate: String = "",
    val selectedSpaces: MutableList<Int> = mutableListOf(),

    val pdfLocation: String = "",
    val existAndNoDir: Boolean = false,

    val error: Boolean = false,
    val isLoading: Boolean = false,

    val isLoadingToShow: Boolean = false,

    val pdfVerticalReaderState: VerticalPdfReaderState = VerticalPdfReaderState(
        ResourceType.Local(Uri.parse(""))
    ),

    val toastMessage: String = ""
)