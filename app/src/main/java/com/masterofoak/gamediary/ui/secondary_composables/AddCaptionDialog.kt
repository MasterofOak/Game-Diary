package com.masterofoak.gamediary.ui.secondary_composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*

@Composable
fun AddCaptionDialog(onDismissRequest: () -> Unit, onConfirm: (String) -> Unit) {
    var caption by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val isInputValid: () -> Boolean = { caption.trim().isNotEmpty() && caption.length <= 100 }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (isInputValid()) {
                    onConfirm(caption)
                    onDismissRequest()
                } else isError = true
            }) {
                Text("Add")
            }
        },
        title = {
            Text("Add caption")
        },
        text = {
            TextField(
                value = caption,
                onValueChange = { caption = it },
                singleLine = true,
                isError = isError,
                placeholder = { Text("My #1 boss...") },
                supportingText = {
                    if (isError) Text("Name should be at least 1 character long and don't exceed 100 characters")
                    else Text("")
                }
            )
        }
    )
}