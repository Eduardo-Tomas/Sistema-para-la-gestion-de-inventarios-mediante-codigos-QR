package com.example.inventorymanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.inventorymanager.model.ModelObjectDTO

@Composable
fun EditObjectCard(
    navController: NavHostController,
    objectViewModel: ObjectViewModel,
) {
    Scaffold(
        topBar = {
            TitleBar(
                stringResource(R.string.update)
            )
        }
    ) { innerPadding ->
        EditObjectForm(
            navController = navController,
            onCancel = { navController.popBackStack() },
            objectViewModel = objectViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun EditObjectForm(
    navController: NavHostController,
    onCancel: () -> Unit,
    objectViewModel: ObjectViewModel,
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
        InputObjectN(
            objectViewModel = objectViewModel,
            type = stringResource(R.string.input_type_text),
            value = objectViewModel.objectState.objectEditName,
            placeholder = stringResource(id = R.string.name),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        InputObjectP(
            objectViewModel = objectViewModel,
            type = stringResource(R.string.input_type_number),
            value = objectViewModel.objectState.objectEditPrice,
            placeholder = stringResource(R.string.price),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
        ) {
            OSButton(
                onClick = {
                    val state = objectViewModel.objectState
                    val name = state.objectEditName
                    val price = state.objectEditPrice.toFloat()
                    val objectID = state.objectEdit.objectID.toString()

                    val modelObjectDTO = ModelObjectDTO(name, price)
                    objectViewModel.updateObject(modelObjectDTO, objectID)
                    navController.popBackStack()
                },
                label = stringResource(R.string.update)
            )

            OSButton(
                onClick = onCancel,
                label = stringResource(R.string.cancel)
            )
        }
    }
}

@Composable
fun InputObjectN(
    objectViewModel: ObjectViewModel,
    type: String,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { objectViewModel.changeObjectName(it) },
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

@Composable
fun InputObjectP(
    objectViewModel: ObjectViewModel,
    type: String,
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { objectViewModel.changeObjectPrice(it) },
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