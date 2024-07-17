package com.example.quiz.model

data class Question (
    var name: String,
    val answers: List<Answer>,
    var userAdded: Boolean = false // Default is false for initially created questions
)