package com.example.quiz.screens.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.apiConnection.openAiConnection.Message
import com.example.quiz.apiConnection.openAiConnection.OpenAiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenViewModel @Inject constructor(
    private val openAiRepository: OpenAiRepository
) : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun sendPrompt(prompt: String) {
        viewModelScope.launch {
            try {
                Log.d("OpenViewModel", "Sending prompt: $prompt")
                val messages = parseMessages(prompt)
                val result = openAiRepository.getCompletions(100, messages)
                if (result.isSuccessful) {
                    val responseBody = result.body()
                    Log.d("OpenViewModel", "Received response: $responseBody")
                    _response.value = responseBody?.choices?.get(0)?.message?.content ?: "No response"
//                    _response.value = responseBody?.choices?.firstOrNull()?.text ?: "No response"
                } else {
                    Log.e("OpenViewModel", "Error response: ${result.errorBody()?.string()}")
                    _response.value = "Error: ${result.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                Log.e("OpenViewModel", "Exception: ${e.message}", e)
                _response.value = "Error: ${e.message}"
            }
        }
    }

    private fun parseMessages(rawString: String): List<Message> {
        // Example parsing logic, adjust as needed
        return rawString.split("\n").map { line ->
            Message(role = "user", content = line)
        }
    }
}