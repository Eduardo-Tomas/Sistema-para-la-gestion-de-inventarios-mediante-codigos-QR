package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.inventorymanager.R

@Composable
fun UpdateStockLimit(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(id = R.string.amount)
            )
        }
    ) { innerPadding ->
        StockLimitForm(
            notificationViewModel = notificationViewModel,
            onContinueClicked = {
                if (isNumber(notificationViewModel.notificationState.stockLimitSelected)) {
                    notificationViewModel.updateStockLimit()
                    navController.popBackStack()
                }

            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}