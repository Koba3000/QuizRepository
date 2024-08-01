package com.example.quiz.screens.categories


import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.screens.category.CategoryViewModel
import com.example.quiz.R
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
            Text(text = stringResource(id = R.string.category_added_successfully))
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