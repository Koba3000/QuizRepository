package com.example.quiz.view

sealed class Screens(val route: String) {
    object LoginScreen : Screens("login_screen")
    object StartScreen : Screens("start_screen")
    object CategoryScreen : Screens("category_screen")
    object QuizScreen : Screens("quiz_screen/{categoryId}")
    object AnswersScreen : Screens("answers_screen")
    object CategoryForms : Screens("category_forms/{categoryId}")
    object NewCategoryConfirmationScreen : Screens("new_category_confirmation_screen")
    object CategoriesToEdit : Screens("categories_to_edit")
    object SettingsScreen : Screens("settings_screen")
}
