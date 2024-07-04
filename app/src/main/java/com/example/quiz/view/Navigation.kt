package com.example.quiz.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz.AnswersScreen
import com.example.quiz.CategoryScreen
import com.example.quiz.QuizScreen
import com.example.quiz.StartScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.StartScreen.route) {
        composable(Screens.StartScreen.route) {
            StartScreen(navController) }
        composable(Screens.CategoryScreen.route) {
            CategoryScreen(navController) }
        composable(
            route = Screens.QuizScreen.route,
            arguments = listOf(navArgument(name = "categoryId"){
                type = NavType.StringType
                defaultValue = "0"
            })
        ){
            QuizScreen(navController)
        }
        composable(Screens.AnswersScreen.route) {
            AnswersScreen(navController)
        }
    }
}