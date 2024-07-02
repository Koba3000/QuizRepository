package com.example.quiz

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiz.view.Screens
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun QuizScreen(
    navController: NavController
) {

    val questionsCategory1 = Category(listOf(
        Question("question 1 (A)", listOf(
            Answer("a1 (A)",true),
            Answer("a2 (A)",false),
            Answer("a3 (A)",false),
            Answer("a4 (A)",true),
            Answer("a5 (A)",true),
            Answer("a6 (A)",false))
        ),
        Question("question 2 (A)", listOf(
            Answer("a1 (A)",false),
            Answer("a2 (A)",false),
            Answer("a3 (A)",false),
            Answer("a4 (A)",true))
        ),
        Question("question 3 (A)", listOf(
            Answer("a1 (A)", false),
            Answer("a2 (A)",true),
            Answer("a3 (A)",false),
            Answer("a4 (A)",false)
        ))
    ))
    val questionsCategory2 = Category(listOf(
        Question("Question 1 (B)", listOf(
            Answer("a1(B)", true),
            Answer("a2(B)", false),
            Answer("a2(B)", false),
            Answer("a2(B)", false))
        ),
        Question("Question 2 (B)", listOf(
            Answer("a1(B)", true),
            Answer("a2(B)", false),
            Answer("a2(B)", false),
            Answer("a2(B)", false))
        )
    ))
    val questionsCategory3 = Category(listOf(
        Question("Question 1 (C)", listOf(
            Answer("a1 (C)", true),
            Answer("a2 (C)", false),
            Answer("a1 (C)", false),
            Answer("a2 (C)", false)
        )),
        Question("Question 2 (C)", listOf(
            Answer("a1 (C)", true),
            Answer("a2 (C)", false),
            Answer("a1 (C)", false),
            Answer("a2 (C)", false)
        )),
        Question("Question 3 (C)", listOf(
            Answer("a1 (C)", true),
            Answer("a2 (C)", false),
            Answer("a1 (C)", false),
            Answer("a2 (C)", false)
        )),
        Question("Question 4 (C)", listOf(
            Answer("a1 (C)", true),
            Answer("a2 (C)", false),
            Answer("a1 (C)", false),
            Answer("a2 (C)", false)
        ))
    ))

    val categories = listOf(questionsCategory1, questionsCategory2, questionsCategory3)

    val categoryId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("categoryId")
        ?.toIntOrNull() ?: 0

    var currentQuestion by remember { mutableIntStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var showCorrectAnswers by remember { mutableStateOf(false) }
    var showFilledForm by remember { mutableStateOf(false) }

    Log.d("EntryScreen", "currentRoute: $categoryId")
    Log.d("current category questions: \n", categories[categoryId].category[0].question)

    Column {

        QuizQuestion(quizQuestion = categories[categoryId].category[currentQuestion].question)

        val answerStates = remember {
            categories.flatMap { category ->
                category.category.map { question ->
                    question.answers.map { mutableStateOf(false) }
                }
            }
        }

        for (i in 0 until categories[categoryId].category[currentQuestion].answers.size) {
            QuizAnswer(
                quizAnswer = categories[categoryId].category[currentQuestion].answers[i].answer,
                isCorrectAnswer = categories[categoryId].category[currentQuestion].answers[i].isCorrect,
                isSelected = answerStates[currentQuestion][i]
            )
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
                    if (currentQuestion < categories[categoryId].category.size - 1) {
                        currentQuestion++
                    }
                },
                shape = RectangleShape
            ) {
                Text(text = ">>>")
            }
            Text(text = (currentQuestion + 1).toString() + "/" + categories[categoryId].category.size,
                style = TextStyle(fontSize = 20.sp))

            Button(
                modifier = Modifier
                    .height(100.dp)
                    .padding(10.dp),
                onClick = {
                    correctAnswers = calculateCorrectAnswers(categories[categoryId], answerStates)
                    showCorrectAnswers = true
                    showFilledForm = true
                    Log.d("Correct Answers", correctAnswers.toString())

                },
                shape = RectangleShape
            ) {
                Text(text = "send form")
            }
        }
        if (showCorrectAnswers) {
            Text(text = "Correct Answers: $correctAnswers")
        }
        if (showFilledForm) {
            DisplayFilledForm(categories[categoryId], answerStates)
        }

        Row {
            NavigationButton(
                navController = navController,
                route = Screens.CategoryScreen.route,
                text = "back to categories"
            )
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
//                checkedColor = if (isCorrectAnswer) Color.Green else Color.Red,
                checkedColor = Color.Gray,
                uncheckedColor = Color.Gray
            )
        )
        Text(text = quizAnswer)
    }
}

fun calculateCorrectAnswers(category: Category, answerStates: List<List<MutableState<Boolean>>>): Int {
    var correctAnswers = 0
    for (questionIndex in category.category.indices) {
        for (answerIndex in category.category[questionIndex].answers.indices) {
            if (answerStates[questionIndex][answerIndex].value == category.category[questionIndex].answers[answerIndex].isCorrect) {
                correctAnswers++
            }
        }
    }
    return correctAnswers
}

@Composable
fun DisplayFilledForm(category: Category, userAnswers: List<List<MutableState<Boolean>>>) {
    for (questionIndex in category.category.indices) {
        Text(text = "Question: " + category.category[questionIndex].question)
        for (answerIndex in category.category[questionIndex].answers.indices) {
            Text(text = "Answer: " + category.category[questionIndex].answers[answerIndex].answer)
            Text(text = "User answer: " + if (userAnswers[questionIndex][answerIndex].value) "TRUE" else "FALSE")
            Text(text = "Correct answer: " + if (category.category[questionIndex].answers[answerIndex].isCorrect) "TRUE" else "FALSE")
        }
    }
}

