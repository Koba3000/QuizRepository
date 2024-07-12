package com.example.quiz.ApiConnection

import com.squareup.moshi.Json

data class CategoryDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String,
    @Json(name = "questions") val questions: List<QuestionDto>? = null
)

data class QuestionDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String,
    @Json(name = "answers") val answers: List<AnswerDto>? = null
)

data class AnswerDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "title") val answer: String,
    @Json(name = "isCorrect") val isCorrect: Boolean
)
