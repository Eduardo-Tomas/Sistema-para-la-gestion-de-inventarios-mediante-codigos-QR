package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R

@Composable
fun NotificationPeriod(
    notificationViewModel: NotificationViewModel,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.set_period)
            )
        }
    ) { innerPadding ->
        PeriodForm(
            notificationViewModel = notificationViewModel,
            onSaveClicked = {
                if (isNumber(notificationViewModel.notificationState.notificationPeriod)) {

                    val interval = notificationViewModel.notificationState.notificationPeriod.toLong()

                    if (notificationViewModel.notificationState.notificationPeriodType == 1) {
                        notificationViewModel.sendStockLimitNotification(interval)
                    } else {
                        notificationViewModel.sendAbsenceNotification(interval)
                    }

                    navController.popBackStack()
                    navController.popBackStack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun PeriodForm(
    notificationViewModel: NotificationViewModel,
    onSaveClicked: () -> Unit,
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputPeriod(
                notificationViewModel = notificationViewModel,
                type = stringResource(R.string.input_type_number),
                value = notificationViewModel.notificationState.notificationPeriod,
                placeholder = stringResource(R.string.period),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.days),
                color = colorResource(id = R.color.accent),
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        OSButton(
            onClick = {
                onSaveClicked()
            },
            label = stringResource(R.string.save)
        )
    }
}

@Composable
fun InputPeriod(
    notificationViewModel: NotificationViewModel,
    type: String,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { notificationViewModel.setNotificationPeriod(it) },
        label = {
            Text(
                text = placeholder,
                color = colorResource(id = R.color.accentDark),
                fontSize = 16.sp
            )
        },
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = colorResource(id = R.color.accent),
            unfocusedBorderColor = colorResource(id = R.color.primaryLight),
            unfocusedContainerColor = colorResource(id = R.color.primaryLight),
            focusedTextColor = colorResource(id = R.color.accent),
            focusedBorderColor = colorResource(id = R.color.secondary),
            focusedContainerColor = colorResource(id = R.color.primaryLight),
            cursorColor = colorResource(id = R.color.secondary),
            selectionColors = TextSelectionColors(
                handleColor = colorResource(id = R.color.transparent),
                backgroundColor = colorResource(id = R.color.secondary)
            )
        ),
        keyboardOptions = when (type) {
            "number" -> KeyboardOptions(keyboardType = KeyboardType.Number)
            else -> KeyboardOptions(keyboardType = KeyboardType.Text)
        }
    )
}