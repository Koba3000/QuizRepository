package com.example.quiz

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quiz.AppData
import com.example.quiz.Category
import com.example.quiz.view.Screens
import kotlinx.coroutines.launch

@Composable
fun AnswersScreen(
    navController: NavController
){
    val scrollState = rememberScrollState()

    Scaffold { paddingValues ->
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
                    Text(text = "Correct Answers: $totalPoints")
                    DisplayFilledForm(category = category, userAnswers = answerStates)
                    NavigationButton(
                        navController = navController,
                        route = Screens.CategoryScreen.route,
                        text = "back to categories"
                    )
                }

            } else {
                Column {
                    Log.d("AnswersScreen", "Error: No data available. Please try again.")
                    Text(text = "Error: No data available. Please try again.")
                    NavigationButton(
                        navController = navController,
                        route = Screens.CategoryScreen.route,
                        text = "back to categories"
                    )
                }
            }
        }
    }
}


@Composable
fun DisplayFilledForm(category: Category, userAnswers: List<List<MutableState<Boolean>>>) {
    val questionPoints = calculateQuestionPoints(category, userAnswers)
    for (questionIndex in category.category.indices) {
        Text(text = "Question: " + category.category[questionIndex].question +" " + questionPoints[questionIndex] + " points")
        for (answerIndex in category.category[questionIndex].answers.indices) {
            Text(text = "Answer: " + category.category[questionIndex].answers[answerIndex].answer)
            Text(text = "User's answer: " + if (userAnswers[questionIndex][answerIndex].value) "TRUE" else "FALSE")
            Text(text = "Correct answer: " + if (category.category[questionIndex].answers[answerIndex].isCorrect) "TRUE" else "FALSE")
        }
    }
}

fun calculateTotalPoints(category: Category, answerStates: List<List<MutableState<Boolean>>>): Int {
    val questionPoints = calculateQuestionPoints(category, answerStates)
    return questionPoints.sum()
}

fun calculateQuestionPoints(category: Category, answerStates: List<List<MutableState<Boolean>>>): List<Int> {
    val questionPoints = mutableListOf<Int>()
    for (questionIndex in category.category.indices) {
        var allAnswersCorrect = true
        for (answerIndex in category.category[questionIndex].answers.indices) {
            if (answerStates[questionIndex][answerIndex].value != category.category[questionIndex].answers[answerIndex].isCorrect) {
                allAnswersCorrect = false
                break
            }
        }
        questionPoints.add(if (allAnswersCorrect) 1 else 0)
    }
    return questionPoints
}