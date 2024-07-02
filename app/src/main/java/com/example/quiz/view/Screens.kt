package com.example.quiz.view

sealed class Screens(val route: String) {
    object StartScreen : Screens("start_screen")
    object CategoryScreen : Screens("category_screen")
    object QuizScreen : Screens("quiz_screen/{categoryId}")
    object ResultsScreen : Screens("results_screen")
}
