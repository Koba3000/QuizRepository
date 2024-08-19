package com.example.quiz.screens.start.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.quiz.R
import com.example.quiz.ui.theme.QuizButton

@Composable
fun PromptDialog(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val containerColor = Color(0xFF0077b6)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.enter_prompt)) },
        text = {
            Column{
                TextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        },
        confirmButton = {
            QuizButton(text = stringResource(R.string.submit), onClick = onSubmit)
        },
        dismissButton = {
            QuizButton(text= stringResource(R.string.cancel),onClick = onDismiss)
        }
    )
}