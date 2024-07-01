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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.Notification
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun NotificationsCard(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    Scaffold(
        topBar = {
            TitleIconBar(
                titleText = stringResource(R.string.notifications_option),
                imagePainter = painterResource(id = R.drawable.add_fill0_24),
                contentDescription = stringResource(id = R.string.add_icon),
                action = { navController.navigate(InventoryManagerScreen.NotificationList.name) }
            )
        }
    ) { innerPadding ->
        NotificationList(
            notificationViewModel = notificationViewModel,
            onClicked = { navController.navigate(InventoryManagerScreen.UpdateStockLimit.name) },
            onPeriodSelected = {
                navController.navigate(InventoryManagerScreen.NotificationPeriodOption.name)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleIconBar(
    titleText: String,
    imagePainter: Painter,
    contentDescription: String,
    action: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
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
        actions = {
            IconButton(onClick = action) {
                Icon(
                    painter = imagePainter,
                    contentDescription = contentDescription,
                    tint = colorResource(id = R.color.secondary),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        modifier = Modifier.height(50.dp)
    )
}

@Composable
fun NotificationList(
    notificationViewModel: NotificationViewModel,
    onClicked: () -> Unit,
    onPeriodSelected: () -> Unit,
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
        SelectionItem(
            option = stringResource(R.string.adjust_notification_period),
            imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
            description = stringResource(R.string.navigation_icon),
            onClick = {
                onPeriodSelected()
            }
        )

        LazyColumn {
            items(
                notificationViewModel.notificationState.notifications,
                key = {  item ->  item.notificationID }
            ) {
                NotificationItem(notificationViewModel, it, onClicked)
            }
        }
    }
}

@Composable
fun NotificationItem(
    notificationViewModel: NotificationViewModel,
    item: Notification,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
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
            .clickable {
                if (item.notificationTypeID == 1) {
                    notificationViewModel.setStockLimit("")
                    notificationViewModel.setNotificationRowSelected(item.notificationID)
                    onClicked()
                }
            }
    ) {
        Column {
            Row {
                Text(
                    text = item.description,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.accent),
                    modifier = Modifier.padding(start = 8.dp)
                )

                if (item.notificationTypeID == 1) {
                    Text(
                        text = item.limitAmount.toString(),
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.accent),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Row {
                Text(
                    text = "E. ",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.secondary),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = item.spaceName,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.accent),
                )
            }

            Row {
                Text(
                    text = "O. ",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.secondary),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = item.objectName,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.accent),
                )
            }
        }

        Row {
            IconButton(onClick = { openAlertDialog.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_fill0_24),
                    contentDescription = stringResource(R.string.delete_icon),
                    tint = colorResource(id = R.color.secondary)
                )
            }

            Switch(
                checked = item.isActive == 1,
                onCheckedChange = {
                    val newValue = if (it) 1 else 0
                    //println("Switch: ${it}")

                    val notifications = notificationViewModel.notificationState.notifications

                    // "Copy"
                    val previousNotifications = notifications.map { element ->
                        element
                    }

                    val updatedNotifications = notifications.map { element ->
                        if (element.notificationID == item.notificationID) {
                            element.copy(isActive = newValue)
                        } else {
                            element
                        }
                    }
                    notificationViewModel.setNotifications(updatedNotifications)

                    notificationViewModel.onSwitchChanged(item, newValue, previousNotifications)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.accent),
                    checkedTrackColor = colorResource(id = R.color.secondary),
                    uncheckedThumbColor = colorResource(id = R.color.accent),
                    uncheckedTrackColor = colorResource(id = R.color.primaryLight),
                ),
                modifier = Modifier.scale(0.70f)
            )
        }
    }

    if (openAlertDialog.value) {
        AlertDialogObjectAndSpace(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                notificationViewModel.deleteNotification(item.notificationID.toString())
            },
            dialogTitle = stringResource(R.string.detele_notification),
            dialogText = stringResource(R.string.confirm_delete_notification),
            icon = Icons.Default.Info
        )
    }
}