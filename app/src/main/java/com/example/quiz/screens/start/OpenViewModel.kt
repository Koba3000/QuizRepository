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
import java.io.File
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
                Log.d("OpenViewModel", "Parsed messages: $messages")
                val result = openAiRepository.getCompletions(500, messages)
                if (result.isSuccessful) {
                    val responseBody = result.body()
                    Log.d("OpenViewModel", "Received response: $responseBody")
                    val content = responseBody?.choices?.get(0)?.message?.content
                    Log.d("OpenViewModel", "Content: $content")

                    _response.value = content ?: "No response"
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

//    private fun parseMessages(rawString: String): List<Message> {
//        // Example parsing logic, adjust as needed
//        return rawString.split("\n").map { line ->
//            Message(role = "user", content = line)
//        }
//    }

    private fun parseMessages(rawString: String): List<Message> {
        val additionalInfo = """
            Create only a JSON object (inclusive lack of spaces) for a quiz app with the following structure:
            {"name":"Maths","questions":[{"name":"2 + 2?","answers":[{"title":"4","isCorrect":true}]},{"name":"2+2*2?","answers":[{"title":"4","isCorrect":false}]}]}
        """.trimIndent()

        val combinedPrompt = "$rawString\n\n$additionalInfo"
        return listOf(Message(role = "user", content = combinedPrompt))
    }
}