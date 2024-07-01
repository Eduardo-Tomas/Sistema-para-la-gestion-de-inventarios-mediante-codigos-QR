package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.inventorymanager.R
import com.example.inventorymanager.data.BaseURL

@Composable
fun DetectionsObjectCard(detectionViewModel: DetectionViewModel) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.info)
            )
        }
    ) { innerPadding ->
        ObjectDataCard(
            detectionViewModel = detectionViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ObjectDataCard(
    detectionViewModel: DetectionViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colorResource(id = R.color.primary))
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val code = detectionViewModel.detectionState.objectClicked.objectQR
        val imgURL = "${BaseURL().baseURL}images/objects/${code}.svg"

        QRCodeCard(
            imageUrl = imgURL,
            imageLoader = imageLoader,
            contentDescription = stringResource(R.string.object_qr_code)
        )

        PropertyValueCard(
            property = stringResource(R.string.object_id),
            value = detectionViewModel.detectionState.objectClicked.objectID.toString()
        )

        PropertyValueCard(
            property = stringResource(R.string.object_name),
            value = detectionViewModel.detectionState.objectClicked.objectName
        )

        PropertyValueCard(
            property = stringResource(R.string.object_price),
            value = detectionViewModel.detectionState.objectClicked.objectPrice.toString()
        )

        PropertyValueCard(
            property = stringResource(R.string.object_code),
            value = detectionViewModel.detectionState.objectClicked.objectQR
        )
    }
}