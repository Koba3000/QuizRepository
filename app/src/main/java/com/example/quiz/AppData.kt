package com.example.quiz

import androidx.compose.runtime.MutableState
import com.example.quiz.ApiConnection.CategoryDto
import com.example.quiz.model.Category
import com.example.quiz.screens.quiz.convertDtoToCategory

object AppData {
    var quizAttempt: QuizAttempt? = null
}

class QuizAttempt(categoryDto: CategoryDto?) {
    var categories: Category? = null
    var userAnswers: List<List<MutableState<Boolean>>>? = null

    init {
        this.categories = convertDtoToCategory(categoryDto)
    }
}
