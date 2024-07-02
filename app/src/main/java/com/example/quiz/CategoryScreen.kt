package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quiz.view.Screens

@Composable
fun CategoryScreen(
    navController: NavController
){
    CategoryButtons(navController)
}

@Composable
fun CategoryButtons(navController: NavController){
    Column {
        for (i in 0 .. 2){
            val category = i + 1
            CategoryNavigationButton(
                navController = navController,
                text = "Category $category",
                buttonId = i.toString())
        }
        NavigationButton(navController,Screens.StartScreen.route,"go back to start screen")
    }
}

@Composable
fun CategoryNavigationButton(navController: NavController, text: String, buttonId: String) {
    val url = Screens.QuizScreen.route.replace("{categoryId}", buttonId)
    Button(onClick = {
        navController.navigate(url)
    }) {
        Text(text = text,
            style = TextStyle(fontSize = 20.sp)
        )
    }
}