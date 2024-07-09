package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quiz.view.Screens
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController
){

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QUIZ APP") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        CategoryButtons(navController = navController, paddingValues = paddingValues)
    }
}

@Composable
fun CategoryButtons(navController: NavController, paddingValues: PaddingValues){
    Column(modifier = Modifier
        .padding(paddingValues)
    ){
        for (i in 0 .. 2){
            val category = i + 1
            CategoryNavigationButton(
                navController = navController,
                text = "Category $category",
                buttonId = i.toString())
        }
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