package com.example.quiz.model

data class Answer (
        val answer: String,
        val isCorrect: Boolean,
        val userAdded: Boolean = false // Default is false for initially created answers
)
