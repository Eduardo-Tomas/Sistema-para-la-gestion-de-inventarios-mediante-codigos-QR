package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun ObjectScreen(
    navController: NavHostController,
    objectViewModel: ObjectViewModel,
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.objects_title)
            )
        }
    ) { innerPadding ->
        ObjectPanel(
            objectViewModel,
            onAddClicked = { navController.navigate(InventoryManagerScreen.NewObject.name) },
            onScanClicked =  { navController.navigate(InventoryManagerScreen.OneObjectCapture.name) },
            onEditClicked = { navController.navigate(InventoryManagerScreen.EditObject.name) },
            onViewClicked = { navController.navigate(InventoryManagerScreen.ViewObjectInfo.name) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(titleText: String) {
    Column {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = colorResource(id = R.color.primary),
                titleContentColor = colorResource(id = R.color.accent),
            ),
            title = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(titleText, fontSize = 24.sp)
                }
            },
            modifier = Modifier.height(50.dp)
        )
    }
}

@Composable
fun ObjectPanel(
    objectViewModel: ObjectViewModel,
    onAddClicked: () -> Unit,
    onScanClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onViewClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(colorResource(id = R.color.primary))
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            OSIconButton(
                imagePainter = painterResource(id = R.drawable.add_fill0_24),
                label = stringResource(R.string.add),
                imageSize = 60.dp,
                description = stringResource(R.string.add_icon),
                onButtonClicked = onAddClicked,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            OSIconButton(
                imagePainter = painterResource(id = R.drawable.outline_photo_camera_24),
                label = stringResource(R.string.view_object_info),
                imageSize = 60.dp,
                description = stringResource(R.string.camera_icon),
                onButtonClicked = onScanClicked,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
        }

        LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
            items(
                objectViewModel.objectState.objects,
                key = {  item ->  item.objectID }
            ) {
                ObjectItem(objectViewModel, it, onEditClicked, onViewClicked)
            }
        }
    }
}

@Composable
fun OSIconButton(
    imagePainter: Painter,
    label: String,
    imageSize: Dp,
    description: String,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = colorResource(id = R.color.accent)

    Button(
        onClick = onButtonClicked,
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primaryLight)
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = imagePainter,
                contentDescription = description,
                tint = color,
                modifier = Modifier.size(imageSize)
            )

            Text(
                text = label,
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun ObjectItem(
    objectViewModel: ObjectViewModel,
    item: ModelObject,
    onEdit: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier.clickable {
        objectViewModel.onObjectClicked(item.objectID.toString())
        onView()
    }
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.primaryLight))
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = item.objectID.toString(),
                fontSize = 16.sp,
                color = colorResource(id = R.color.accent),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = item.objectName,
                fontSize = 16.sp,
                color = colorResource(id = R.color.accent),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row {
            IconButton(onClick = {
                objectViewModel.onEditObject(item)
                onEdit()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_fill0_24),
                    contentDescription = stringResource(R.string.edit_icon),
                    tint = colorResource(id = R.color.secondary)
                )
            }
            IconButton(onClick = { openAlertDialog.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_fill0_24),
                    contentDescription = stringResource(R.string.delete_icon),
                    tint = colorResource(id = R.color.secondary)
                )
            }
        }
    }

    if (openAlertDialog.value) {
        AlertDialogObjectAndSpace(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                objectViewModel.deleteObject(item.objectID.toString())
            },
            dialogTitle = stringResource(R.string.delete_object),
            dialogText = stringResource(R.string.confirm_delete_object),
            icon = Icons.Default.Info
        )
    }
}