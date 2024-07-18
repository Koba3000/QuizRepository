package com.example.quiz

import androidx.compose.runtime.MutableState
import com.example.quiz.ApiConnection.CategoryDto
import com.example.quiz.model.Answer
import com.example.quiz.model.Category
import com.example.quiz.model.Question

object AppData {
    var quizAttempt: QuizAttempt? = null
}

class QuizAttempt(categoryDto: CategoryDto?) {
    var categories: Category? = null
    var userAnswers: List<List<MutableState<Boolean>>>? = null

    init {
        this.categories = com.example.quiz.screens.convertDtoToCategory(categoryDto)
    }
}
