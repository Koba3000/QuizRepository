package com.example.quiz.apiConnection.openAiConnection.dto

import com.example.quiz.model.Category
import com.squareup.moshi.Json

data class CategoryDto(
    @Json(name = "name") val name: String,
    @Json(name = "questions") val questions: List<QuestionDto>
) {
    fun toCategory(): Category {
        return Category(
            name = this.name,
            questionList = this.questions.map { it.toQuestion() }
        )
    }
}