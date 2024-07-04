package com.example.quiz

import androidx.compose.runtime.MutableState

object AppData {
    var quizAttempt: QuizAttempt? = null
}

class QuizAttempt {
    var categories: Category? = null
    var userAnswers: List<List<MutableState<Boolean>>>? = null
}
