package com.example.quiz.model

data class Question (
    var name: String,
    val answers: List<Answer>
)