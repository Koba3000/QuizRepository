package com.example.quiz.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiz.view.Screens
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quiz.ApiConnection.CategoryDto
import com.example.quiz.ApiConnection.CategoryViewModel
import com.example.quiz.AppData
import com.example.quiz.QuizAttempt
import com.example.quiz.R
import com.example.quiz.model.Answer
import com.example.quiz.model.Category
import com.example.quiz.model.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var currentQuestion by remember { mutableIntStateOf(0) }
    val answerStates = viewModel.answerStates.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {
                        currentQuestion = 0
                        navController.navigate(Screens.CategoryScreen.route)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }

            val categoryId = navController
                .currentBackStackEntry
                ?.arguments
                ?.getString("categoryId")
                ?.toIntOrNull() ?: 0

            Column {

                val category = viewModel.categories.firstOrNull { it.id == categoryId }

                category?.questions?.forEach { question ->
                    Log.d("QuizScreen", "Question: ${question.name}")
                    question.answers?.forEach { answer ->
                        Log.d("QuizScreen", "Answer: ${answer.answer}, Is Correct: ${answer.isCorrect}")
                    } ?: Log.d("QuizScreen", "No answers available for this question")
                } ?: Log.d("QuizScreen", "No questions available for this category")

                if (category != null) {
                    QuizQuestion(quizQuestion = category.questions?.get(currentQuestion)?.name ?: "No question available")
                }

                if (category != null && currentQuestion >= 0 && currentQuestion < (category.questions?.size ?: 0)) {
                    val question = category.questions?.getOrNull(currentQuestion)
                    question?.answers?.let { answers ->
                        if (answers.isNotEmpty()) {
                            answers.forEachIndexed { index, answer ->
                                if (index >= 0 && index < answers.size) {
                                    QuizAnswer(
                                        quizAnswer = answer.answer,
                                        isCorrectAnswer = answer.isCorrect,
                                        isSelected = answerStates[currentQuestion].getOrNull(index) ?: mutableStateOf(false)
                                    )
                                }
                            }
                        }
                    }
                }

                Row {
                    Button(
                        modifier = Modifier
                            .height(100.dp)
                            .padding(10.dp),
                        onClick = {
                            if (currentQuestion > 0) {
                                currentQuestion--
                            }
                        },
                        shape = RectangleShape
                    ) {
                        Text(text = "<<<")
                    }

                    Button(
                        modifier = Modifier
                            .height(100.dp)
                            .padding(10.dp),
                        onClick = {
                            if (currentQuestion < (category?.questions?.size ?: 0) - 1) {
                                currentQuestion++
                            }
                        },
                        shape = RectangleShape
                    ) {
                        Text(text = ">>>")
                    }
                    Text(
                        text = (currentQuestion + 1).toString() + "/" + (category?.questions?.size ?: 0),
                        style = TextStyle(fontSize = 20.sp)
                    )

                    Button(
                        modifier = Modifier
                            .height(100.dp)
                            .padding(10.dp),
                        onClick = {
                            val categoryDto: CategoryDto? = category// Obtain your CategoryDto object
                            val _category: Category = convertDtoToCategory(categoryDto)
                            AppData.quizAttempt = QuizAttempt(categoryDto).apply {
                                this.categories = _category
                                this.userAnswers = answerStates
                            }
                            currentQuestion = 0
                            navController.navigate(Screens.AnswersScreen.route)
                        },
                        shape = RectangleShape
                    ) {
                        Text(text = stringResource(id = R.string.send_form))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizQuestion(quizQuestion: String) {
    Text(text = quizQuestion,
        modifier = Modifier.padding(10.dp),
        style = TextStyle(fontSize = 25.sp)
        )
}

@Composable
fun QuizAnswer(quizAnswer: String, isCorrectAnswer: Boolean, isSelected: MutableState<Boolean>) {

    Row (modifier = Modifier.size(width=200.dp, height=30.dp)){
        Checkbox(
            checked = isSelected.value,
            onCheckedChange = { isSelected.value = it },
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.White,
                checkedColor = if (isCorrectAnswer) Color.Green else Color.Red,
//                checkedColor = Color.Gray,
                uncheckedColor = Color.Gray
            )
        )
        Text(text = quizAnswer)
    }
}

fun convertDtoToCategory(categoryDto: CategoryDto?): Category {
    if (categoryDto == null) {
        return Category("", listOf())
    }
    return Category(
        name = categoryDto.name,
        questionList = categoryDto.questions?.map { questionDto ->
            Question(
                name = questionDto.name,
                answers = questionDto.answers?.map { answerDto ->
                    Answer(answerDto.answer, answerDto.isCorrect)
                } ?: listOf()
            )
        } ?: listOf()
    )
}



