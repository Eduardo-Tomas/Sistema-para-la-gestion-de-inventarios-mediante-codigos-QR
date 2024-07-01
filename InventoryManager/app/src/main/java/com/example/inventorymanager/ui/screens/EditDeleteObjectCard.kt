package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.example.inventorymanager.R
import com.example.inventorymanager.data.BaseURL
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun EditDeleteObjectCard(
    navController: NavHostController,
    objectViewModel: ObjectViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.info)
            )
        }
    ) { innerPadding ->
        ObjectInfoCard(
            onEdit = { navController.navigate(InventoryManagerScreen.EditObject.name) },
            objectViewModel = objectViewModel,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ObjectInfoCard(
    onEdit: () -> Unit,
    objectViewModel: ObjectViewModel,
    navController: NavHostController,
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

        val code = objectViewModel.objectState.objectRowClicked.objectQR
        val imgURL = "${BaseURL().baseURL}images/objects/${code}.svg"

        QRCodeCard(
            imageUrl = imgURL,
            imageLoader = imageLoader,
            contentDescription = stringResource(R.string.object_qr_code)
        )

        PropertyValueCard(
            property = stringResource(R.string.object_id),
            value = objectViewModel.objectState.objectRowClicked.objectID.toString()
        )

        PropertyValueCard(
            property = stringResource(R.string.object_name),
            value = objectViewModel.objectState.objectRowClicked.objectName
        )

        PropertyValueCard(
            property = stringResource(R.string.object_price),
            value = objectViewModel.objectState.objectRowClicked.objectPrice.toString()
        )

        PropertyValueCard(
            property = stringResource(R.string.object_code),
            value = objectViewModel.objectState.objectRowClicked.objectQR
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
        ) {
            OSButton(
                onClick = {
                    objectViewModel.onEditObject(objectViewModel.objectState.objectRowClicked)
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
                    objectViewModel.deleteObject(
                        objectViewModel.objectState.objectRowClicked.objectID.toString()
                    )
                    navController.popBackStack()
                },
                dialogTitle = stringResource(R.string.delete_object),
                dialogText = stringResource(R.string.confirm_delete_object),
                icon = Icons.Default.Info
            )
        }

    }
}

@Composable
fun QRCodeCard(
    imageUrl: String,
    imageLoader: ImageLoader,
    contentDescription: String
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            imageLoader = imageLoader
        ),
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                top = 4.dp,
                bottom = 16.dp
            )
            .width(100.dp)
            .height(100.dp)
    )
}

@Composable
fun PropertyValueCard(
    property: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(56.dp)
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.primaryLight))
            .fillMaxWidth()
    ) {
        Text(
            text = property,
            fontSize = 16.sp,
            color = colorResource(id = R.color.accent),
            modifier = Modifier.padding(start = 8.dp, end = 40.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = colorResource(id = R.color.accentDark),
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
fun AlertDialogObjectAndSpace(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "",
                tint = colorResource(id = R.color.secondary)
            )
        },
        title = {
            Text(
                text = dialogTitle,
                color = colorResource(id = R.color.accentDark)
            )
        },
        text = {
            Text(
                text = dialogText,
                color = colorResource(id = R.color.accentDark)
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    stringResource(R.string.accept),
                    color = colorResource(id = R.color.secondary)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    stringResource(id = R.string.cancel),
                    color = colorResource(id = R.color.secondary)
                )
            }
        },
        containerColor = colorResource(id = R.color.primaryLight)
    )
}