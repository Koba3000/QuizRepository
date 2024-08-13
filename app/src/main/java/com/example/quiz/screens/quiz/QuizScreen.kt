package com.example.quiz.screens.quiz

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quiz.apiConnection.CategoryDto
import com.example.quiz.screens.categories.CategoryViewModel
import com.example.quiz.AppData
import com.example.quiz.QuizAttempt
import com.example.quiz.R
import com.example.quiz.model.Answer
import com.example.quiz.model.Category
import com.example.quiz.model.Question
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.ui.theme.QuizText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var currentQuestion by remember { mutableIntStateOf(0) }
    val answerStates = viewModel.answerStates.value
    val backgroundColor = Color(0xFFcaf0f8)
    val topBarBackgroundColor = Color(0xFF90e0ef)





    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBackgroundColor
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        QuizText(
                            text = stringResource(id = R.string.app_name),
                            fontSize = FontSizeLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.CategoryScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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
                                            isSelected = answerStates[categoryId]?.get(currentQuestion)?.getOrNull(index) ?: mutableStateOf(false)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row {
                        QuizButton(
                            modifier = Modifier
                                .height(100.dp)
                                .padding(10.dp),
                            onClick = {
                                if (currentQuestion > 0) {
                                    currentQuestion--
                                }
                            },
                            text = "<<<"
                        )

                        QuizButton(
                            modifier = Modifier
                                .height(100.dp)
                                .padding(10.dp),
                            onClick = {
                                if (currentQuestion < (category?.questions?.size ?: 0) - 1) {
                                    currentQuestion++
                                }
                            },
                            text = ">>>"
                        )
                        QuizText(
                            text = (currentQuestion + 1).toString() + "/" + (category?.questions?.size ?: 0),
                            fontSize = 20.sp
                        )

                        QuizButton(
                            modifier = Modifier
                                .height(100.dp)
                                .padding(10.dp),
                            onClick = {
                                val categoryDto: CategoryDto? = category
                                val _category: Category = convertDtoToCategory(categoryDto)
                                AppData.quizAttempt = QuizAttempt(categoryDto).apply {
                                    this.categories = _category
                                    this.userAnswers = answerStates[categoryId] ?: emptyList()
                                }
                                currentQuestion = 0
                                navController.navigate(Screens.AnswersScreen.route)
                            },
                            text = stringResource(id = R.string.send_form)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizQuestion(quizQuestion: String) {
    QuizText(text = quizQuestion,
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
        QuizText(text = quizAnswer)
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



