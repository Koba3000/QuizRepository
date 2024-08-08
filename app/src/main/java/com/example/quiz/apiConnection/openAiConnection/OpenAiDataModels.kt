package com.example.quiz.apiConnection.openAiConnection

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAiRequest(
    @Json(name = "max_tokens") val maxTokens: Int,
    @Json(name = "model") val model: String = "gpt-4o-mini",
    @Json(name = "temperature") val temperature: Double,
    @Json(name = "messages") val messages: List<Message>
)

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class OpenAiResponse(
    val id: String,
    @Json(name = "object") val objectType: String,
    val created: Long,
    val model: String,
    @Json(name = "system_fingerprint") val systemFingerprint: String,
    val choices: List<Choice>,
    val usage: Usage
)

@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "index") val index: Int,
    @Json(name = "message") val message: Message,
    @Json(name = "logprobs") val logprobs: Any?,
    @Json(name = "finish_reason") val finishReason: String,
    @Json(name = "text") val text: String?
)

@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name = "prompt_tokens") val promptTokens: Int,
    @Json(name = "completion_tokens") val completionTokens: Int,
    @Json(name = "total_tokens") val totalTokens: Int
)