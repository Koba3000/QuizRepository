package com.example.quiz.apiConnection.openAiConnection.dto

import com.example.quiz.model.Answer
import com.squareup.moshi.Json

data class AnswerDto(
    @Json(name = "title") val answer: String,
    @Json(name = "isCorrect") val isCorrect: Boolean
) {
    fun toAnswer(): Answer {
        return Answer(
            answer = this.answer,
            isCorrect = this.isCorrect
        )
    }
}