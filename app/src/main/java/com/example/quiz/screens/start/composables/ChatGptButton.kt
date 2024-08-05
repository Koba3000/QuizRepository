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

@Composable
fun ChatGptButton(
    openViewModel: OpenViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var prompt by remember { mutableStateOf("") }
    val response by openViewModel.response.observeAsState("")

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(onClick = { showDialog = true }) {
            Text(text = "Ask ChatGPT")
        }

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


