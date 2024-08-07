package com.example.quiz.screens.start.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PromptDialog(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Enter Prompt") },
        text = {
            Column {
                TextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = onSubmit) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}