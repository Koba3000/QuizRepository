package com.example.quiz.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz.screens.answer.AnswersScreen
import com.example.quiz.screens.categories.CategoryScreen
import com.example.quiz.screens.categories.CategoriesToEditScreen
import com.example.quiz.screens.categories.NewCategoryConfirmationScreen
import com.example.quiz.screens.categories.CategoryForms
import com.example.quiz.screens.quiz.QuizScreen
import com.example.quiz.screens.settings.SettingsScreen
import com.example.quiz.screens.start.StartScreen
import com.example.quiz.screens.login.LoginScreen
import com.example.quiz.screens.login.LoginViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()

    val startDestination = if (loginViewModel.isUserLoggedIn()) {
        Screens.StartScreen.route
    } else {
        Screens.LoginScreen.route
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(
            Screens.LoginScreen.route
        ){
            LoginScreen(navController = navController)
        }

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

        composable(
            route = Screens.SettingsScreen.route
        ) {
            SettingsScreen(navController = navController)
        }
    }
}