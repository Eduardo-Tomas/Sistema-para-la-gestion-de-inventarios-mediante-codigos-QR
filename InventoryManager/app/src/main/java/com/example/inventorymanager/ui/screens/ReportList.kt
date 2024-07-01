package com.example.inventorymanager.ui.screens

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventorymanager.R
import com.example.inventorymanager.model.PDF
import com.example.inventorymanager.model.SpaceCheck
import com.example.inventorymanager.ui.InventoryManagerScreen

@Composable
fun ReportList(
    navController: NavHostController,
    pdfViewModel: PDFViewModel
) {
    Scaffold(
        topBar = {
            TitleBarRL(
                stringResource(R.string.select_report)
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
            val ctx = LocalContext.current

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                InputStartDate(
                    pdfViewModel = pdfViewModel,
                    value = pdfViewModel.pdfState.startDate,
                    placeholder = "${stringResource(R.string.start_date)} (${stringResource(R.string.date_format)})",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )

                InputEndDate(
                    pdfViewModel = pdfViewModel,
                    value = pdfViewModel.pdfState.endDate,
                    placeholder = "${stringResource(R.string.end_date)} (${stringResource(id = R.string.date_format)})",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier.weight(2f)
                    .fillMaxWidth()
            ) {
                SpaceSelection(pdfViewModel = pdfViewModel)
            }

            Column(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth()
            ) {
                SelectionItem(
                    option = stringResource(R.string.spaces_report),
                    imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
                    description = stringResource(R.string.navigation_icon),
                    onClick = {
                        val startDate = pdfViewModel.pdfState.startDate
                        val endDate = pdfViewModel.pdfState.endDate

                        if (startDate != "" && endDate != "") {
                            if (validateDateFormat(startDate) && validateDateFormat(endDate)) {
                                pdfViewModel.changeStartDate(reformatDate(startDate))
                                pdfViewModel.changeEndDate(reformatDate(endDate))

                                configureDownload(
                                    pdfViewModel,
                                    navController,
                                    1,
                                    "spacesReport.pdf",
                                    ctx
                                )
                            }
                        } else if (startDate != "" && endDate == "") {
                            if (validateDateFormat(startDate)) {
                                pdfViewModel.changeStartDate(reformatDate(startDate))

                                configureDownload(
                                    pdfViewModel,
                                    navController,
                                    1,
                                    "spacesReport.pdf",
                                    ctx
                                )
                            }

                        } else if (startDate == "" && endDate == "") {
                            configureDownload(
                                pdfViewModel,
                                navController,
                                1,
                                "spacesReport.pdf",
                                ctx
                            )
                        }
                    }
                )

                SelectionItem(
                    option = stringResource(R.string.detailed_report_by_space),
                    imagePainter = painterResource(id = R.drawable.navigate_next_fill0_24),
                    description = stringResource(R.string.navigation_icon),
                    onClick = {
                        val startDate = pdfViewModel.pdfState.startDate
                        val endDate = pdfViewModel.pdfState.endDate

                        if (startDate != "" && endDate != "") {
                            if (validateDateFormat(startDate) && validateDateFormat(endDate)) {
                                pdfViewModel.changeStartDate(reformatDate(startDate))
                                pdfViewModel.changeEndDate(reformatDate(endDate))

                                configureDownload(
                                    pdfViewModel,
                                    navController,
                                    2,
                                    "detailedReportBySpace.pdf",
                                    ctx
                                )
                            }
                        } else if (startDate != "" && endDate == "") {
                            if (validateDateFormat(startDate)) {
                                pdfViewModel.changeStartDate(reformatDate(startDate))

                                configureDownload(
                                    pdfViewModel,
                                    navController,
                                    2,
                                    "detailedReportBySpace.pdf",
                                    ctx
                                )
                            }

                        } else if (startDate == "" && endDate == "") {
                            configureDownload(
                                pdfViewModel,
                                navController,
                                2,
                                "detailedReportBySpace.pdf",
                                ctx
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBarRL(titleText: String) {
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
                Text(titleText, fontSize = 16.sp)
            }
        },
        modifier = Modifier.height(50.dp)
    )
}

@Composable
fun InputStartDate(
    pdfViewModel: PDFViewModel,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { pdfViewModel.changeStartDate(it) },
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun InputEndDate(
    pdfViewModel: PDFViewModel,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { pdfViewModel.changeEndDate(it) },
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun SpaceSelection(
    pdfViewModel: PDFViewModel
) {
    var items by remember {
        mutableStateOf(
            pdfViewModel.pdfState.spaces.map {
                SpaceCheck(
                    spaceID = it.spaceID,
                    spaceName = it.spaceName,
                    isSelected = false
                )
            }
        )
    }

    // Select all
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable {
                if (allSelected(items)) {
                    pdfViewModel.setSelectedSpaces(mutableListOf())

                    // Update view
                    items = items.map {
                        it.copy(isSelected = false)
                    }
                } else {
                    val selectedSpaces = items.map {
                        it.spaceID
                    }
                    pdfViewModel.setSelectedSpaces(selectedSpaces.toMutableList())

                    // Update view
                    items = items.map {
                        it.copy(isSelected = true)
                    }
                }
            }
    ) {
        Text(
            text = stringResource(R.string.select_all),
            color = colorResource(id = R.color.secondary)
        )
    }

    LazyColumn {
        items(items, key = { item -> item.spaceID }) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .padding(bottom = 8.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = colorResource(id = R.color.primaryLight))
                    .fillMaxWidth()
                    .clickable {
                        items = items.map { element ->
                            if (element.spaceID == it.spaceID) {
                                element.copy(isSelected = !element.isSelected)
                            } else {
                                element
                            }
                        }

                        val selectedSpaces: MutableList<Int> = mutableListOf()

                        items.forEach {
                            if (it.isSelected) {
                                selectedSpaces.add(it.spaceID)
                            }
                        }
                        pdfViewModel.setSelectedSpaces(selectedSpaces)
                    }
            ) {
                Row {
                    Text(
                        text = it.spaceID.toString(),
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.accent),
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Text(
                        text = it.spaceName,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.accent),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (it.isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.check_fill0_24),
                        contentDescription = stringResource(R.string.selected_icon),
                        tint = colorResource(id = R.color.secondary),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun SelectionItem(
    option: String,
    imagePainter: Painter,
    description: String,
    onClick: () -> Unit,
) {
    val color = colorResource(id = R.color.secondary)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(id = R.color.primaryLight))
            .height(56.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = option,
            fontSize = 16.sp,
            color = colorResource(id = R.color.accent),
            modifier = Modifier.padding(start = 8.dp)
        )

        Icon(
            painter = imagePainter,
            contentDescription = description,
            tint = color,
        )
    }
}

fun configureDownload(
    pdfViewModel: PDFViewModel,
    navController: NavHostController,
    reportType: Int,
    reportName: String,
    context: Context
) {
    pdfViewModel.onReportSelected(reportType)
    pdfViewModel.setReportName(reportName)
    pdfViewModel.setLocation("")
    pdfViewModel.setExistAndNoDir(false)

    getReport(pdfViewModel, context)

    navController.navigate(InventoryManagerScreen.PDFViewer.name)
}

fun getReport(pdfViewModel: PDFViewModel, context: Context) {
    val pdfNameExt = pdfViewModel.pdfState.reportName
    val pdfType = pdfViewModel.pdfState.reportType

    val startDate = pdfViewModel.pdfState.startDate
    val endDate = pdfViewModel.pdfState.endDate
    val selectedSpaces = pdfViewModel.pdfState.selectedSpaces
    val pdfData = PDF(startDate, endDate, selectedSpaces)

    /*pdfViewModel.changeStartDate("")
    pdfViewModel.changeEndDate("")*/

    pdfViewModel.getReport(pdfData, pdfNameExt, pdfType, context)

    // pdfViewModel.setSelectedSpaces(mutableListOf())
}

fun allSelected(items: List<SpaceCheck>): Boolean {
    items.forEach {
        if (!it.isSelected) {
            return false
        }
    }

    return true
}

fun validateDateFormat(date: String): Boolean {
    val regex = """^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\d{4}$""".toRegex()
    return regex.matches(date)
}

// Receives a date in the form dd-mm-yyyy and returns that date in the format yyyy-mm-dd
fun reformatDate(date: String): String {
    val components = date.split("-")
    return "${components[2]}-${components[1]}-${components[0]}"
}