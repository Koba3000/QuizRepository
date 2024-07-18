package com.example.quiz.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.ApiConnection.CategoryViewModel
import com.example.quiz.view.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategoryConfirmationScreen(
    navController: NavController,
    categoryName: String,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categoryData = viewModel.categories.find { it.name == categoryName }

    Log.d("NewCategoryConfirmationScreen", "categoryData: $categoryData")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QUIZ APP") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.StartScreen.route)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Category added successfully")
            Text("Category Name: ${categoryData?.name}")
            categoryData?.questions?.forEachIndexed { index, question ->
                Text("Q${index + 1}: ${question.name}")
                question.answers?.forEach { answer ->
                    Text("- ${answer.answer} ${if (answer.isCorrect) "(Correct Answer)" else "(Wrong Answer)"}")
                }
            }
        }
    }
}