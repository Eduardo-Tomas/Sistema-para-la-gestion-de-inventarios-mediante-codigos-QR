package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.inventorymanager.R
import com.example.inventorymanager.data.BaseURL
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun EditDeleteSpaceCard(
    navController: NavHostController,
    spaceViewModel: SpaceViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.info)
            )
        }
    ) { innerPadding ->
        SpaceInfoCard(
            spaceViewModel = spaceViewModel,
            navController = navController,
            onEdit = { navController.navigate(InventoryManagerScreen.EditSpace.name) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SpaceInfoCard(
    navController: NavHostController,
    spaceViewModel: SpaceViewModel,
    onEdit: () -> Unit,
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
        val openAlertDialog = remember { mutableStateOf(false) }

        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val code = spaceViewModel.spaceState.spaceRowClicked.spaceQR
        val imgURL = "${BaseURL().baseURL}images/spaces/${code}.svg"

        QRCodeCard(
            imageUrl = imgURL,
            imageLoader = imageLoader,
            contentDescription = stringResource(R.string.space_qr_code)
        )

        PropertyValueCard(
            property = stringResource(R.string.space_id),
            value = spaceViewModel.spaceState.spaceRowClicked.spaceID.toString()
        )

        PropertyValueCard(
            property = stringResource(R.string.space_name),
            value = spaceViewModel.spaceState.spaceRowClicked.spaceName
        )

        PropertyValueCard(
            property = stringResource(R.string.space_qr),
            value = spaceViewModel.spaceState.spaceRowClicked.spaceQR
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
        ) {
            OSButton(
                onClick = {
                    spaceViewModel.onEditSpace(
                        spaceViewModel.spaceState.spaceRowClicked
                    )
                    onEdit()
                },
                label = stringResource(R.string.edit)
            )
            OSButton(
                onClick = {
                    openAlertDialog.value = true
                },
                label = stringResource(R.string.delete)
            )
        }

        if (openAlertDialog.value) {
            AlertDialogObjectAndSpace(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    spaceViewModel.deleteSpace(
                        spaceViewModel.spaceState.spaceRowClicked.spaceID.toString()
                    )
                    navController.popBackStack()
                },
                dialogTitle = stringResource(R.string.delete_space),
                dialogText = stringResource(R.string.confirm_delete_space),
                icon = Icons.Default.Info
            )
        }
    }
}