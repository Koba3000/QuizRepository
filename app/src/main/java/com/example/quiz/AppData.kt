package com.example.quiz

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.quiz.apiConnection.CategoryDto
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
        this.userAnswers = categories?.questionList?.map { question ->
            question.answers.map { mutableStateOf(false) }
        }
        Log.d("QuizAttempt", "Initialized with categories: $categories")
        Log.d("QuizAttempt", "Initialized with userAnswers: $userAnswers")
    }
}
