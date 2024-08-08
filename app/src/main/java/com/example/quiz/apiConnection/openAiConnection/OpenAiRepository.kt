package com.example.quiz.apiConnection.openAiConnection

import android.util.Log
import retrofit2.Response
import javax.inject.Inject

class OpenAiRepository @Inject constructor(private val openAiService: OpenAiService) {

    suspend fun getCompletions(messages: List<Message>, maxTokens: Int = 1000,  temperature: Double = 0.7): Response<OpenAiResponse> {
        val request = OpenAiRequest(maxTokens, messages = messages, temperature = temperature)
        Log.d("OpenAiRepository", "Sending request: $request")
        return openAiService.getCompletions(request)
    }
}