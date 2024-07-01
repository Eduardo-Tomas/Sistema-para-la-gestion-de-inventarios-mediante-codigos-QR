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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.inventorymanager.R
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun NotificationTypeCard(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    Scaffold(
        topBar = {
            TitleBarRL(
                stringResource(R.string.select_notification)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.primary))
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectionItem(
                option = stringResource(R.string.stock_limit),
                imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
                description = stringResource(R.string.navigation_icon),
                onClick = {
                    notificationViewModel.setNotificationType(1)
                    notificationViewModel.setStockLimit("")
                    navController.navigate(InventoryManagerScreen.InputStockLimit.name)
                }
            )

            SelectionItem(
                option = stringResource(R.string.absence),
                imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
                description = stringResource(R.string.navigation_icon),
                onClick = {
                    notificationViewModel.setNotificationType(2)
                    navController.navigate(InventoryManagerScreen.SetSpaceNotification.name)
                }
            )
        }
    }
}