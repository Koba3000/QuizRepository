package com.example.quiz.screens.start.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quiz.screens.start.OpenViewModel
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.R

@Composable
fun ChatGptButton(
    openViewModel: OpenViewModel = hiltViewModel(),
    onResponse: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var prompt by remember { mutableStateOf("") }
    val response by openViewModel.response.observeAsState("")

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        QuizButton(text = stringResource(id = R.string.chatGptText), onClick = { showDialog = true })

        if (showDialog) {
            PromptDialog(
                prompt = prompt,
                onPromptChange = { prompt = it },
                onDismiss = { showDialog = false },
                onSubmit = {
                    openViewModel.sendPrompt(prompt)
                    showDialog = false
                }
            )
        }

        LaunchedEffect(response) {
            if (response.isNotEmpty()) {
                onResponse(response)
            }
        }
    }
}


