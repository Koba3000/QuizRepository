package com.example.quiz.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.quiz.AppData
import com.example.quiz.R
import com.example.quiz.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswersScreen(
    navController: NavController
){
    val scrollState = rememberScrollState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
            val category = AppData.quizAttempt?.categories
            val answerStates = AppData.quizAttempt?.userAnswers

            if (category != null && answerStates != null) {
                val totalPoints =
                    calculateTotalPoints(category = category, answerStates = answerStates)
                Column {
                    Text(text = stringResource(id = R.string.correct_answers, totalPoints))
                    DisplayFilledForm(category = category, userAnswers = answerStates)
                }
            } else {
                Column {
                    Text(text = stringResource(id = R.string.error_no_data_available))
                }
            }
        }
    }
}

@Composable
fun DisplayFilledForm(category: Category, userAnswers: List<List<MutableState<Boolean>>>) {
    val questionPoints = calculateQuestionPoints(category, userAnswers)
    for (questionIndex in category.questionList.indices) {
        Text(text = stringResource(
            id = R.string.question,
            category.questionList[questionIndex].name, questionPoints[questionIndex]))

        for (answerIndex in category.questionList[questionIndex].answers.indices) {
            Text(text = stringResource(id = R.string.answer, category.questionList[questionIndex].answers[answerIndex].answer))
            Text(text = stringResource(id = R.string.users_answer, if (userAnswers[questionIndex][answerIndex].value) stringResource(id = R.string.true_value) else stringResource(id = R.string.false_value)))
            Text(text = stringResource(id = R.string.correct_answer, if (category.questionList[questionIndex].answers[answerIndex].isCorrect) stringResource(id = R.string.true_value) else stringResource(id = R.string.false_value)))
        }
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