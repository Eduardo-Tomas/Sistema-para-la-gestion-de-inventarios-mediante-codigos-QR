package com.example.inventorymanager.ui.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventorymanager.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun BeginningCard(
    onCaptureClicked: () -> Unit,
    onViewObjectsClicked: () -> Unit,
    onViewSpacesClicked: () -> Unit,
    onViewDetectionsClicked: () -> Unit,
    showReportTypes: () -> Unit,
    onViewNotificationsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.inventory_management)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .background(colorResource(id = R.color.primary))
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(
                    start = 16.dp,
                    top = 4.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row {
                    OptionButton(
                        imagePainter = painterResource(id = R.drawable.outline_inventory_2_24),
                        label = stringResource(id = R.string.objects_option),
                        description = stringResource(R.string.objects_icon),
                        action = onViewObjectsClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp)
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OptionButton(
                        imagePainter = painterResource(id = R.drawable.outline_villa_24),
                        label = stringResource(id = R.string.spaces_option),
                        description = stringResource(R.string.spaces_icon),
                        action = onViewSpacesClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp)
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Row {
                    OptionButton(
                        imagePainter = painterResource(id = R.drawable.qr_code_fill0_24),
                        label = stringResource(R.string.detections),
                        description = stringResource(R.string.detections_icon),
                        action = onViewDetectionsClicked,
                        modifier = Modifier
                            .height(140.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OptionButton(
                        imagePainter = painterResource(id = R.drawable.feed_fill0_24),
                        label = stringResource(R.string.reporting_option),
                        description = stringResource(R.string.reports_icon),
                        action = showReportTypes,
                        modifier = Modifier
                            .height(140.dp)
                            .weight(1f)
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Row {
                    OptionButton(
                        imagePainter = painterResource(id = R.drawable.outline_notifications_24),
                        label = stringResource(id = R.string.notifications_option),
                        description = stringResource(R.string.notifications_icon),
                        action = onViewNotificationsClicked,
                        modifier = Modifier
                            .height(140.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            ScannerButton(
                imagePainter = painterResource(id = R.drawable.outline_photo_camera_24),
                description = stringResource(R.string.camera_icon),
                action = { onCaptureClicked() }
            )
        }
    }
}

@Composable
fun OptionButton(
    imagePainter: Painter,
    label: String,
    description: String,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = colorResource(id = R.color.accent)

    Button(
        onClick = action,
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
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = label,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerButton(
    imagePainter: Painter,
    description: String,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Ask permission to use the camera
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    val color = colorResource(id = R.color.accent)

    // The initial value of the variable is false
    // var showCameraCard by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (!cameraPermissionState.status.isGranted) {
                cameraPermissionState.launchPermissionRequest()
            } else {
                action()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.secondary)
        ),
        modifier = modifier
    ) {
        Icon(
            painter = imagePainter,
            contentDescription = description,
            tint = color,
            modifier = Modifier.size(40.dp)
        )
    }
}