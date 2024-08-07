package com.example.quiz.screens.start.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quiz.screens.start.OpenViewModel
import com.example.quiz.ui.theme.QuizButton

@Composable
fun ChatGptButton(
    openViewModel: OpenViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var prompt by remember { mutableStateOf("") }
    val response by openViewModel.response.observeAsState("")

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        QuizButton(text = "Ask ChatGPT", onClick = { showDialog = true })

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

        if (response.isNotEmpty()) {
            Text(text = "Response: $response", modifier = Modifier.padding(top = 16.dp))
        }
    }
}


