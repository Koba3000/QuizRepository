package com.example.quiz.apiConnection.openAiConnection

import retrofit2.Response
import javax.inject.Inject

class OpenAiRepository @Inject constructor(private val openAiService: OpenAiService) {

    suspend fun getCompletions(maxTokens: Int, messages: List<Message>): Response<OpenAiResponse> {
        val request = OpenAiRequest(maxTokens, messages = messages, temperature = 0.7)
        return openAiService.getCompletions(request)
    }
}