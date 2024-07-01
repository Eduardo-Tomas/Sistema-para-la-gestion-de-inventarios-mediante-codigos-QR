package com.example.inventorymanager.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.TableDetection
import com.example.inventorymanager.operations.DateTime
import com.example.inventorymanager.ui.InventoryManagerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetectionsInSpaceCard(
    navController: NavHostController,
    detectionViewModel: DetectionViewModel
) {
    detectionViewModel.getMissingObjects()

    Scaffold(
        topBar = {
            TitleBar(
                "${ stringResource(id = R.string.detections) } ${ detectionViewModel.detectionState.spaceRowClicked.spaceName }"
            )
        }
    ) { innerPadding ->
        DetectionsPanel(
            detectionViewModel = detectionViewModel,
            onViewClicked = {
                navController.navigate(InventoryManagerScreen.ViewDetectedMissingObject.name)
            },
            modifier = Modifier.padding(innerPadding)
        )

        val lastDateStr = detectionViewModel.detectionState.lastDetectionsDate

        // To show or not the message
        if (lastDateStr != "") {
            val dateTime = DateTime()
            val currentDate = dateTime.getCurrentDate()

            val lastDate = dateTime.normalizeDate(lastDateStr)

            currentDate?.let {
                if (lastDate.isBefore(/*currentDate*/ it)) {
                    val duration = Toast.LENGTH_LONG

                    val toast = Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.outdated_detections_message),
                        duration
                    )
                    toast.show()

                    detectionViewModel.setLastDetectionsDate("")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetectionsPanel(
    detectionViewModel: DetectionViewModel,
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
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            DetectedObjects(
                detectionViewModel = detectionViewModel,
                onViewClicked = onViewClicked
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            NotFoundObjects(
                detectionViewModel = detectionViewModel,
                onViewClicked = onViewClicked
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetectedObjects(
    detectionViewModel: DetectionViewModel,
    onViewClicked: () -> Unit
) {
    Text(
        text = stringResource(R.string.detected_objects),
        fontSize = 16.sp,
        color = colorResource(id = R.color.accent)
    )
    LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
        items(
            detectionViewModel.detectionState.lastDetectionsBySpace,
            key = {  item ->  item.detectionID }
        ) {
            ObjectDetectedItem(detectionViewModel, it, onViewClicked)
        }
    }
}

@Composable
fun NotFoundObjects(
    detectionViewModel: DetectionViewModel,
    onViewClicked: () -> Unit
) {
    Text(
        text = stringResource(R.string.missing_objects),
        fontSize = 16.sp,
        color = colorResource(id = R.color.accent)
    )
    LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
        items(
            detectionViewModel.detectionState.missingObjectsBySpace,
            key = { item -> item.objectID }
        ) {
            MissingItem(detectionViewModel, it, onViewClicked)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ObjectDetectedItem(
    detectionViewModel: DetectionViewModel,
    item: TableDetection,
    onView: () -> Unit,
    modifier: Modifier = Modifier
        .height(56.dp)
        .clickable {
            val objectID = item.objectID
            val objectName = item.objectName
            val objectPrice = item.objectPrice
            val objectQR = item.objectQR

            val objectC = ModelObject(
                objectID,
                objectName,
                objectPrice,
                objectQR
            )

            detectionViewModel.onObjectClicked(objectC)
            onView()
        }
) {
    val d = DateTime()

    Row(
        /*horizontalArrangement = Arrangement.Start,*/
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

        Text(
            text = d.normalizeDate(item.captureDate).toString(),
            fontSize = 16.sp,
            color = colorResource(id = R.color.accent),
            modifier = Modifier.padding(start = 8.dp)
        )

        Text(
            text = item.captureTime,
            fontSize = 16.sp,
            color = colorResource(id = R.color.accent),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun MissingItem(
    detectionViewModel: DetectionViewModel,
    item: ModelObject,
    onView: () -> Unit,
    modifier: Modifier = Modifier
        .height(56.dp)
        .clickable {
            detectionViewModel.onObjectClicked(item)

            onView()
        }
) {
    Row(
        /*horizontalArrangement = Arrangement.Start,*/
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