package com.example.quiz.view

sealed class Screens(val route: String) {
    object StartScreen : Screens("start_screen")
    object CategoryScreen : Screens("category_screen")
    object QuizScreen : Screens("quiz_screen/{categoryId}")
    object AnswersScreen : Screens("answers_screen")
    object NewCategoryScreen : Screens("new_category_screen")
    object NewCategoryConfirmationScreen : Screens("new_category_confirmation_screen")
}
