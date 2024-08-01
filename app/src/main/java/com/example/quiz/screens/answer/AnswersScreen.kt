package com.example.quiz.screens.answer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiz.AppData
import com.example.quiz.R
import com.example.quiz.model.Category
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswersScreen(
    navController: NavController
){
    val scrollState = rememberScrollState()
    val backgroundColor = Color(0xFFcaf0f8)
    val topBarBackgroundColor = Color(0xFF90e0ef)

    Scaffold (
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
                    IconButton(onClick = { navController.navigateUp() }) {
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
                val category = AppData.quizAttempt?.categories
                val answerStates = AppData.quizAttempt?.userAnswers

                if (category != null && answerStates != null) {
                    val totalPoints =
                        calculateTotalPoints(category = category, answerStates = answerStates)
                    Column {
                        QuizText(text = stringResource(id = R.string.correct_answers, totalPoints), fontSize = FontSizeLarge)
                        DisplayFilledForm(category = category, userAnswers = answerStates)
                    }
                } else {
                    Column {
                        QuizText(text = stringResource(id = R.string.error_no_data_available))
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayFilledForm(category: Category, userAnswers: List<List<MutableState<Boolean>>>) {
    val questionPoints = calculateQuestionPoints(category, userAnswers)
    for (questionIndex in category.questionList.indices) {
        QuizText(text = stringResource(
            id = R.string.question,
            category.questionList[questionIndex].name, questionPoints[questionIndex]))

        for (answerIndex in category.questionList[questionIndex].answers.indices) {
            QuizText(text = stringResource(id = R.string.answer, category.questionList[questionIndex].answers[answerIndex].answer))
            QuizText(text = stringResource(id = R.string.users_answer, if (userAnswers[questionIndex][answerIndex].value) stringResource(id = R.string.true_value) else stringResource(id = R.string.false_value)))
            QuizText(text = stringResource(id = R.string.correct_answer, if (category.questionList[questionIndex].answers[answerIndex].isCorrect) stringResource(id = R.string.true_value) else stringResource(id = R.string.false_value)))
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }
}

fun calculateTotalPoints(category: Category, answerStates: List<List<MutableState<Boolean>>>): Int {
    val questionPoints = calculateQuestionPoints(category, answerStates)
    return questionPoints.sum()
}

fun calculateQuestionPoints(category: Category, answerStates: List<List<MutableState<Boolean>>>): List<Int> {
    val questionPoints = mutableListOf<Int>()
    for (questionIndex in category.questionList.indices) {
        var allAnswersCorrect = true
        for (answerIndex in category.questionList[questionIndex].answers.indices) {
            if (answerStates[questionIndex][answerIndex].value != category.questionList[questionIndex].answers[answerIndex].isCorrect) {
                allAnswersCorrect = false
                break
            }
        }
        questionPoints.add(if (allAnswersCorrect) 1 else 0)
    }
    return questionPoints
}