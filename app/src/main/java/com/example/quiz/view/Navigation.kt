package com.example.quiz.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz.screens.AnswersScreen
import com.example.quiz.screens.CategoryScreen
import com.example.quiz.screens.CategoriesToEditScreen
import com.example.quiz.screens.NewCategoryConfirmationScreen
import com.example.quiz.screens.CategoryForms
import com.example.quiz.screens.QuizScreen
import com.example.quiz.screens.StartScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.StartScreen.route) {
        composable(
            Screens.StartScreen.route
        ){
            StartScreen(navController)
        }
        composable(
            route = Screens.CategoryForms.route,
            arguments = listOf(
                navArgument(name = "categoryId"){
                type = NavType.StringType
                defaultValue = "0"
            })
        ){
            CategoryForms(navController=navController)
        }
        composable(
            Screens.CategoryScreen.route
        ){
            CategoryScreen(navController = navController)
        }
        composable(
            route = Screens.QuizScreen.route,
            arguments = listOf(
                navArgument(name = "categoryId"){
                type = NavType.StringType
                defaultValue = "0"
            })
        ){
            QuizScreen(navController=navController)
        }
        composable(
            route = Screens.AnswersScreen.route
        ){
            AnswersScreen(navController)
        }
        composable(
            route = Screens.NewCategoryConfirmationScreen.route + "/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) {
            val categoryName = it.arguments?.getString("categoryName") ?: ""
            NewCategoryConfirmationScreen(navController = navController, categoryName = categoryName)
        }
        composable(
            route = Screens.CategoriesToEdit.route
        ) {
            CategoriesToEditScreen(navController = navController)
        }
    }
}