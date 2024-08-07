package com.example.quiz.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.R
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategoryConfirmationScreen(
    navController: NavController,
    categoryName: String,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categoryData = viewModel.categories.find { it.name == categoryName }
    val topBarBackgroundColor = Color(0xFF90e0ef)
    val backgroundColor = Color(0xFFcaf0f8)
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { QuizText("QUIZ APP") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.StartScreen.route)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBackgroundColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundColor)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                QuizText(text = stringResource(id = R.string.category_added_successfully), fontSize = FontSizeLarge)
                QuizText(text = "Category Name: ${categoryData?.name}")
                categoryData?.questions?.forEachIndexed { index, question ->
                    QuizText(text = "Q${index + 1}: ${question.name}")
                    question.answers?.forEach { answer ->
                        QuizText(text = "- ${answer.answer} ${if (answer.isCorrect) "(Correct Answer)" else "(Wrong Answer)"}")
                    }
                }
            }
        }
    }
}