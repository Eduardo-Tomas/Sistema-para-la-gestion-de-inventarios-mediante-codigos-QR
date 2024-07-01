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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.ui.InventoryManagerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanOptionsCard(
    scanViewModel: ScanViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.scan_options)
            )
        }
    ) { innerPadding ->
        ScanForm(
            scanViewModel = scanViewModel,
            navController = navController,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanForm(
    scanViewModel: ScanViewModel,
    navController: NavHostController,
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
            option = stringResource(R.string.scan_spaces_and_objects),
            imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
            description = stringResource(R.string.navigation_icon),
            onClick = {
                scanViewModel.setScannedSpaceQRCode(null)
                navController.navigate(InventoryManagerScreen.Sc.name)
            }
        )

        Text(
            text = stringResource(R.string.select_a_space_first),
            color = colorResource(id = R.color.secondary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp)
        )

        val onSpaceDetected: (Space) -> Unit = { space ->
            scanViewModel.onSpaceDetected(space.spaceID)
            scanViewModel.setScannedSpaceQRCode(space.spaceID.toString())
        }
        ChooseSpaceCard(
            spaces = scanViewModel.scanState.spaceObjects,
            navigate = { navController.navigate(InventoryManagerScreen.Sc.name) },
            onClick = onSpaceDetected
        )

    }
}

@Composable
fun ChooseSpaceCard(
    spaces: List<Space>,
    navigate: () -> Unit,
    onClick: (Space) -> Unit
) {
    LazyColumn {
        items(
            items = spaces,
            key = {  item ->  item.spaceID }
        ) {
            SpaceOption(
                it,
                onClick = {
                    onClick(it)
                    navigate()
                }
            )
        }
    }
}

@Composable
fun SpaceOption(
    item: Space,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(56.dp)
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.primaryLight))
            .fillMaxWidth()
            .clickable {
                onClick()
            }
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