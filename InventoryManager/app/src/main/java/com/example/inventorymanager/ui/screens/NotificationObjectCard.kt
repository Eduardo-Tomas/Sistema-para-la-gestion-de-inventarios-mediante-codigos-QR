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
import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.NotificationDTO

@Composable
fun NotificationObjectCard(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.objects_title)
            )
        }
    ) { innerPadding ->
        ObjectSelectionCard(
            notificationViewModel = notificationViewModel,
            onObjectClicked = {
                val notificationType = notificationViewModel.notificationState.notificationTypeSelected

                if (notificationType == 1) {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                } else {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ObjectSelectionCard(
    notificationViewModel: NotificationViewModel,
    onObjectClicked: () -> Unit,
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
                notificationViewModel.notificationState.objects,
                key = {  item ->  item.objectID }
            ) {
                ObjectNotificationItem(notificationViewModel, it, onObjectClicked)
            }
        }
    }
}

@Composable
fun ObjectNotificationItem(
    notificationViewModel: NotificationViewModel,
    item: ModelObject,
    onObjectClicked: () -> Unit,
    modifier: Modifier = Modifier.height(56.dp)
        .clickable {
            notificationViewModel.onObjectClicked(item)

            val state = notificationViewModel.notificationState
            val objectID = state.objectIDSelected
            val spaceID = state.spaceIDSelected
            val notificationTypeID = state.notificationTypeSelected

            val limitAmount =  if (notificationTypeID == 1) {
                state.stockLimitSelected.toInt()
            } else
            {
                0
            }

            val isActive = 1

            val notification = NotificationDTO(
                objectID,
                spaceID,
                notificationTypeID,
                limitAmount,
                isActive
            )

            notificationViewModel.postNotification(notification)

            onObjectClicked()
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
}