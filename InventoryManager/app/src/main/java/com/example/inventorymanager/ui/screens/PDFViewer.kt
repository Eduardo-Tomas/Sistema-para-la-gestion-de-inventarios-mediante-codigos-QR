package com.example.inventorymanager.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventorymanager.R
import com.example.inventorymanager.model.PDF
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.rizzi.bouquet.VerticalPDFReader

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PDFViewerCard(pdfViewModel: PDFViewModel) {
    val pdfNameExt = pdfViewModel.pdfState.reportName
    var pdfName = pdfNameExt.substringBefore(".")

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        val downloadPermissionState = rememberPermissionState(
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        LaunchedEffect(Unit) {
            if (!downloadPermissionState.status.isGranted) {
                downloadPermissionState.launchPermissionRequest()
            }
        }
    }

    if (pdfName.length > 30) {
        pdfName = pdfName.substring(0, 29)
    }

    Scaffold(
        topBar = {
            TitleBarI(
                titleText = pdfName,
                onDownload = {
                    val startDate = pdfViewModel.pdfState.startDate
                    val endDate = pdfViewModel.pdfState.endDate
                    val selectedSpaces = pdfViewModel.pdfState.selectedSpaces
                    val pdfData = PDF(startDate, endDate, selectedSpaces)

                    pdfViewModel.downloadReport(
                        pdfData,
                        pdfName,
                        pdfViewModel.pdfState.reportType
                    )
                }
            )
        }
    ) { innerPadding ->
        if (pdfViewModel.pdfState.existAndNoDir) {
            Box(modifier = Modifier.padding(innerPadding)) {
                PDFCard2(pdfViewModel = pdfViewModel)
            }
        }

        if (pdfViewModel.pdfState.isLoadingToShow) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(colorResource(id = R.color.primary))
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = colorResource(id = R.color.secondary),
                    trackColor = colorResource(id = R.color.primaryLight),
                )
            }
        }

        if (pdfViewModel.pdfState.toastMessage != "") {
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(
                LocalContext.current,
                pdfViewModel.pdfState.toastMessage,
                duration
            )
            toast.show()

            pdfViewModel.setToastMessage("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBarI(titleText: String, onDownload: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = colorResource(id = R.color.accent)
        ),
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(titleText, fontSize = 16.sp)
            }
        },
        actions = {
            IconButton(onClick = onDownload) {
                Icon(
                    painter = painterResource(id = R.drawable.download_fill0_24),
                    contentDescription = stringResource(R.string.download_icon),
                    tint = colorResource(id = R.color.secondary),
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        modifier = Modifier.height(50.dp)
    )
}

@Composable
fun PDFCard2(pdfViewModel: PDFViewModel) {
    VerticalPDFReader(
        state = pdfViewModel.pdfState.pdfVerticalReaderState,
        modifier = Modifier
            .fillMaxSize()
    )
}