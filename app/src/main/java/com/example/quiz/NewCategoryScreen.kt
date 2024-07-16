package com.example.quiz

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.ApiConnection.CategoryViewModel
import com.example.quiz.model.Answer
import com.example.quiz.model.Question
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val scrollState = rememberScrollState()

    var categoryName by remember { mutableStateOf("") }
    var categoryQuestions by remember { mutableStateOf(listOf<Question>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Category") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ){
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    categoryQuestions = categoryQuestions + Question("", listOf())
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Question")
            }
            categoryQuestions.forEachIndexed { index, question ->
                QuestionPanel(
                    question = question,
                    onQuestionChange =
                    { updatedQuestion ->
                        val updatedQuestions = categoryQuestions.toMutableList()
                        updatedQuestions[index] = updatedQuestion
                        categoryQuestions = updatedQuestions
                    },
                    categoryName = categoryName)
                Spacer(modifier = Modifier.height(8.dp))
            }

            var errorMessage by remember { mutableStateOf("") }

            Button(
                onClick = {
                    if (categoryName.isNotBlank() && categoryQuestions.isNotEmpty() && categoryQuestions.all { question ->
                            question.answers.isNotEmpty() && question.answers.all { answer ->
                                answer.answer.isNotBlank()
                            }
                        }) {
                        Log.d("NewCategoryScreen", categoryName)
                        Log.d("NewCategoryScreen", categoryQuestions.toString())
                        viewModel.addNewCategory(categoryName, categoryQuestions)
                        errorMessage = ""

                        val route = Screens.NewCategoryConfirmationScreen.route + "/$categoryName"
                        navController.navigate(route)
                    } else {
                        errorMessage = "Invalid input: Empty category name, questions, or answers"
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Send Category")
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun QuestionPanel(question: Question, onQuestionChange: (Question) -> Unit, categoryName: String) {
    var questionName by remember { mutableStateOf(question.name) }
    var answers by remember { mutableStateOf(question.answers) }
    var showAnswers by remember { mutableStateOf(true) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = questionName,
            onValueChange = { newName ->
                questionName = newName
                onQuestionChange(question.copy(name = newName, answers = answers))
            },
            label = { Text("Question Name") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            val newAnswer = Answer("", false)
            answers = answers + newAnswer
            onQuestionChange(question.copy(name = questionName, answers = answers))
        }) {
            Text("+")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { showAnswers = !showAnswers }) {
            Text(if (showAnswers) "^" else "v")
        }
    }
    if (showAnswers) {
        answers.forEachIndexed { index, answer ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = answer.answer,
                    onValueChange = { newAnswer ->
                        val updatedAnswers = answers.toMutableList()
                        updatedAnswers[index] = answer.copy(answer = newAnswer)
                        answers = updatedAnswers
                        onQuestionChange(question.copy(name = questionName, answers = answers))
                    },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = answer.isCorrect,
                    onCheckedChange = { newValue ->
                        val updatedAnswers = answers.toMutableList()
                        updatedAnswers[index] = answer.copy(isCorrect = newValue)
                        answers = updatedAnswers
                        onQuestionChange(question.copy(name = questionName, answers = answers))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}