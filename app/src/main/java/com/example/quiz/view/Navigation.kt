package com.example.quiz.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quiz.CategoryScreen
import com.example.quiz.QuizScreen
import com.example.quiz.StartScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.StartScreen.route) {
        composable(Screens.StartScreen.route) { StartScreen(navController) }
        composable(Screens.CategoryScreen.route) { CategoryScreen(navController) }
        composable(Screens.QuizScreen.route) { QuizScreen(navController) }
    }
}