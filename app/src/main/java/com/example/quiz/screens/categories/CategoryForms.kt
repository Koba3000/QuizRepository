package com.example.quiz.screens.categories

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.screens.category.CategoryViewModel
import com.example.quiz.CategoryCheck
import com.example.quiz.R
import com.example.quiz.model.Answer
import com.example.quiz.model.Category
import com.example.quiz.model.Question
import com.example.quiz.screens.quiz.convertDtoToCategory
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryForms(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val scrollState = rememberScrollState()
    var isEdit by remember { mutableStateOf(false) }
    var categoryName by remember { mutableStateOf("") }
    var categoryQuestions by remember { mutableStateOf(listOf<Question>()) }
    var categoryIsEmpty by remember { mutableStateOf(CategoryCheck(false, listOf(), mapOf())) }
    val categoryId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("categoryId")
        ?.toIntOrNull() ?: 0

    val category: Category? = if (categoryId != 0) convertDtoToCategory(viewModel.categories.find { it.id == categoryId }) else null

    LaunchedEffect(key1 = category) {
        if (category != null) {
            isEdit = true
            categoryName = category.name
            categoryQuestions = category.questionList
        } else {
            // Initialize with default values if category is null
            categoryName = ""
            categoryQuestions = listOf()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) stringResource(id = R.string.edit_category) else stringResource(id = R.string.new_category)) },
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
                .fillMaxWidth()
                .padding(16.dp)
        ){

            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text(text = stringResource(id = R.string.category_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            if (categoryIsEmpty.isCategoryNameEmpty) {
                Text(
                    text = stringResource(id = R.string.category_name_empty),
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isEdit){
                Button(
                    onClick = {
                        viewModel.deleteCategory(categoryId.toString(), onResult = { success ->
                            if (success) {
                                navController.navigate(Screens.StartScreen.route)
                            }
                        })
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(id = R.string.delete_category))
                }
            }

            Button(
                onClick = {
                    categoryQuestions = categoryQuestions + Question("", listOf(), userAdded = true)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.add_question))
            }

            categoryQuestions.forEachIndexed { index, question ->
                QuestionPanel(
                    question = question,
                    questionIndex = index,
                    onQuestionChange =
                    { updatedQuestion ->
                        val updatedQuestions = categoryQuestions.toMutableList()
                        updatedQuestions[index] = updatedQuestion
                        categoryQuestions = updatedQuestions
                    },
                    onRemoveQuestion = {
                        val updatedQuestions = categoryQuestions.toMutableList().apply {
                            removeAt(index)
                        }
                        categoryQuestions = updatedQuestions
                    },
                    categoryIsEmpty = categoryIsEmpty
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            var errorMessage by remember { mutableStateOf("") }

            if (isEdit){
                Button(
                    onClick = {
                        if (categoryName.isNotBlank() && categoryQuestions.isNotEmpty() && categoryQuestions.all
                            { question ->
                                question.answers.isNotEmpty() && question.answers.all { answer ->
                                    answer.answer.isNotBlank()
                                }
                            }) {
                            viewModel.updateCategory(categoryId.toString(), categoryName, categoryQuestions, onResult = { success ->
                                errorMessage = if (success) "" else R.string.failed_to_update_category.toString()
                                if (success.not()) return@updateCategory
                                val route = Screens.NewCategoryConfirmationScreen.route + "/$categoryName"
                                navController.navigate(route)
                            })
                        }
                        else {
                            categoryIsEmpty = checkCategories(categoryName, categoryQuestions)
                            errorMessage = R.string.invalid_input.toString()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(id = R.string.update_category))
                }
            }
            else {
                Button(
                    onClick = {
                        if (categoryName.isNotBlank() && categoryQuestions.isNotEmpty() && categoryQuestions.all
                            { question ->
                                question.answers.isNotEmpty() && question.answers.all { answer ->
                                    answer.answer.isNotBlank()
                                }
                            }) {
                            viewModel.addNewCategory(categoryName, categoryQuestions, onResult = { success ->
                                errorMessage = if (success) "" else R.string.category_name_occupied.toString()
                                if (success.not()) return@addNewCategory
                                val route = Screens.NewCategoryConfirmationScreen.route + "/$categoryName"
                                navController.navigate(route)
                            })
                        }
                        else {
                            categoryIsEmpty = checkCategories(categoryName, categoryQuestions)
                            errorMessage = R.string.invalid_input.toString()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(id = R.string.send_category))
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.invalid_input),
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
fun QuestionPanel(
    question: Question,
    questionIndex: Int,
    onQuestionChange: (Question) -> Unit,
    onRemoveQuestion: () -> Unit,
    categoryIsEmpty: CategoryCheck
){
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
            label = { Text(text = stringResource(id = R.string.question_name)) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {

            // Set userAdded to true for manually added answers
            val newAnswer = Answer("", false, userAdded = true)
            answers = answers + newAnswer
            onQuestionChange(question.copy(name = questionName, answers = answers))
        }) {
            Text("+")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { showAnswers = !showAnswers }) {
            Text(if (showAnswers) "^" else "v")
        }
        Button(onClick = onRemoveQuestion) {
            Text("-")
        }
    }

    if (categoryIsEmpty.emptyQuestionsIndexes.contains(questionIndex)) {
        Text(
            text = stringResource(id = R.string.question_name_empty),
            color = Color.Red,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
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
//                    label = { Text("Answer ${index + 1}") },
                    label = { Text(text = stringResource(id = R.string.answers_indexed, (index + 1))) },
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
                Button(onClick = {
                    answers = answers.toMutableList().apply {
                        removeAt(index)
                    }
                    onQuestionChange(question.copy(name = questionName, answers = answers))
                }) {
                    Text("-")
                }
            }
            if (categoryIsEmpty.emptyAnswersIndexes[questionIndex]?.contains(index) == true) {
                Text(
                    text = stringResource(id = R.string.answer_empty),
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}

fun checkCategories(categoryName: String, questions: List<Question>): CategoryCheck {
    val isCategoryNameEmpty = categoryName.isBlank()
    val emptyQuestionsIndexes = mutableListOf<Int>()
    val emptyAnswersIndexes = mutableMapOf<Int, MutableList<Int>>()

    questions.forEachIndexed { questionIndex, question ->
        if (question.name.isBlank()) {
            emptyQuestionsIndexes.add(questionIndex)
        }
        val emptyAnswersForQuestion = mutableListOf<Int>()
        question.answers.forEachIndexed { answerIndex, answer ->
            if (answer.answer.isBlank()) {
                emptyAnswersForQuestion.add(answerIndex)
            }
        }
        if (emptyAnswersForQuestion.isNotEmpty()) {
            emptyAnswersIndexes[questionIndex] = emptyAnswersForQuestion
        }
    }

    return CategoryCheck(isCategoryNameEmpty, emptyQuestionsIndexes, emptyAnswersIndexes)
}