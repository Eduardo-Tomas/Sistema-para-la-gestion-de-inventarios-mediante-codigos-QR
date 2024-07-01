package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun NotificationSpaceCard(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.spaces_title)
            )
        }
    ) { innerPadding ->
        SpaceSelectionCard(
            notificationViewModel = notificationViewModel,
            onSpaceClicked = {
                navController.navigate(InventoryManagerScreen.SetObjectNotification.name)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SpaceSelectionCard(
    notificationViewModel: NotificationViewModel,
    onSpaceClicked: () -> Unit,
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
        LazyColumn {
            items(
                notificationViewModel.notificationState.spaces,
                key = {  item ->  item.spaceID }
            ) {
                SpaceNotificationItem(notificationViewModel, it, onSpaceClicked)
            }
        }
    }
}

@Composable
fun SpaceNotificationItem(
    notificationViewModel: NotificationViewModel,
    item: Space,
    onSpaceClicked: () -> Unit,
    modifier: Modifier = Modifier.height(56.dp)
        .clickable {
            notificationViewModel.onSpaceClicked(item)
            onSpaceClicked()
        }
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.primaryLight))
            .fillMaxWidth()
    ) {
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
}