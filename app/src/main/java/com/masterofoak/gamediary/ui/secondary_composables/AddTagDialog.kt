package com.masterofoak.gamediary.ui.secondary_composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddTagDialog(onDialogDismiss: () -> Unit, onConfirmButtonClicked: (String) -> Unit, modifier: Modifier = Modifier) {
    var customTagTextField by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val isInputValid: () -> Boolean = { customTagTextField.trim().isNotEmpty() }
    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        dismissButton = { TextButton(onClick = { onDialogDismiss() }) { Text("Cancel") } },
        confirmButton = {
            TextButton(onClick = {
                if (isInputValid()) onConfirmButtonClicked(customTagTextField) else isError = true
            }) {
                Text("Add")
            }
        },
        title = { Text("Custom tag") },
        text = {
            Column {
                TextField(
                    value = customTagTextField,
                    onValueChange = { customTagTextField = it },
                    singleLine = true,
                    isError = isError,
                    placeholder = { Text("e.g favorite") },
                    supportingText = {
                        if (isError) Text("Name should be at least 1 character long") else Text("")
                    }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview
private fun AddTagDialog_Preview() {
    AddTagDialog(onDialogDismiss = {}, onConfirmButtonClicked = {})
}