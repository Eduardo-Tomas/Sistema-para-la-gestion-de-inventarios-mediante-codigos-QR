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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun SpacesCard(
    navController: NavHostController,
    spaceViewModel: SpaceViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.spaces_title)
            )
        }
    ) { innerPadding ->
        SpacesPanel(
            spaceViewModel,
            onAddClicked = {
                navController.navigate(InventoryManagerScreen.NewSpace.name)
            },
            onScanClicked =  {
                navController.navigate(InventoryManagerScreen.OneSpaceCapture.name)
            },
            onEditClicked = {
                navController.navigate(InventoryManagerScreen.EditSpace.name)
            },
            onViewClicked = {
                navController.navigate(InventoryManagerScreen.ViewSpaceInfo.name)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SpacesPanel(
    spaceViewModel: SpaceViewModel,
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
                label = stringResource(R.string.view_space_info),
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
                spaceViewModel.spaceState.spaces,
                key = {  item ->  item.spaceID }
            ) {
                SpaceItem(spaceViewModel, it, onEditClicked, onViewClicked)
            }
        }
    }
}

@Composable
fun SpaceItem(
    spaceViewModel: SpaceViewModel,
    item: Space,
    onEdit: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier.clickable {
        spaceViewModel.onSpaceClicked(item.spaceID.toString())
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
                text = item.spaceID.toString(),
                fontSize = 16.sp,
                color = colorResource(id = R.color.accent),
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = item.spaceName,
                fontSize = 16.sp,
                color = colorResource(id = R.color.accent),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row {
            IconButton(onClick = {
                spaceViewModel.onEditSpace(item)
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
                spaceViewModel.deleteSpace(item.spaceID.toString())
            },
            dialogTitle = stringResource(R.string.delete_space),
            dialogText = stringResource(R.string.confirm_delete_space),
            icon = Icons.Default.Info
        )
    }
}