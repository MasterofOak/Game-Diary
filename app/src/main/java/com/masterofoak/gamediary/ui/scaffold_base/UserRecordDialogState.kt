package com.masterofoak.gamediary.ui.scaffold_base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.model.Records
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel

data class UserRecordDialogState(
    val userRecordViewModel: UserRecordViewModel,
    val recordType: RecordType,
    val isUpdateMode: Boolean = false,
    val recordToUpdate: Records? = null,
    val onDialogDismiss: () -> Unit = { GlobalDialogManager.dismissAlertDialog() },
    val modifier: Modifier = Modifier.fillMaxWidth(0.9f),
)

object GlobalDialogManager {
    
    var currentDialogState: MutableState<UserRecordDialogState?> = mutableStateOf(null)
        private set
    
    fun showAlertDialog(dialogState: UserRecordDialogState) {
        currentDialogState.value = dialogState
    }
    
    fun dismissAlertDialog() {
        currentDialogState.value = null
    }
}
