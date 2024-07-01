package com.example.inventorymanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.ui.InventoryManagerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetectionsBySpaceCard(
    navController: NavHostController,
    detectionViewModel: DetectionViewModel
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.detections_by_space)
            )
        }
    ) { innerPadding ->
        SpacesList(
            detectionViewModel = detectionViewModel,
            onViewClicked = {
                navController.navigate(InventoryManagerScreen.DetectionsInSpace.name)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpacesList(
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
        LazyColumn {
            items(
                detectionViewModel.detectionState.spaces,
                key = {  item ->  item.spaceID }
            ) {
                SpaceDetectionItem(detectionViewModel, it, onViewClicked)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpaceDetectionItem(
    detectionViewModel: DetectionViewModel,
    item: Space,
    onView: () -> Unit,
    modifier: Modifier = Modifier.height(56.dp)
    .clickable {
        detectionViewModel.onSpaceClicked(item)
        onView()
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