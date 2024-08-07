package com.example.quiz.apiConnection.openAiConnection.dto

import com.example.quiz.model.Question
import com.squareup.moshi.Json

data class QuestionDto(
    @Json(name = "name") val name: String,
    @Json(name = "answers") val answers: List<AnswerDto>
) {
    fun toQuestion(): Question {
        return Question(
            name = this.name,
            answers = this.answers.map { it.toAnswer() }
        )
    }
}