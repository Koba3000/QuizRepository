package com.example.quiz.ApiConnection.OpenAiConnection

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getCompletions(@Body request: OpenAiRequest): Response<OpenAiResponse>
}