package com.example.quiz.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.quiz.CategoryCheck
import com.example.quiz.R
import com.example.quiz.apiConnection.openAiConnection.dto.CategoryDto
import com.example.quiz.model.Answer
import com.example.quiz.model.Category
import com.example.quiz.model.Question
import com.example.quiz.screens.quiz.convertDtoToCategory
import com.example.quiz.screens.start.composables.ChatGptButton
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    val categoryId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("categoryId")
        ?.toIntOrNull() ?: 0

    val category: Category? = if (categoryId != 0) convertDtoToCategory(viewModel.categories.find { it.id == categoryId }) else null
    val topBarBackgroundColor = Color(0xFF90e0ef)
    val backgroundColor = Color(0xFFcaf0f8)

    LaunchedEffect(key1 = category) {
        if (category != null) {
            isEdit = true
            categoryName = category.name
            categoryQuestions = category.questionList
        } else {
            categoryName = ""
            categoryQuestions = listOf()
        }
        loading = false
    }

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
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundColor)
                .fillMaxSize()
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { QuizText(text = stringResource(id = R.string.category_name)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (categoryIsEmpty.isCategoryNameEmpty) {
                        QuizText(text = stringResource(id = R.string.category_name_empty))
                    }

                    if (isEdit) {
                        QuizButton(text = stringResource(id = R.string.delete_category),
                            onClick = {
                                viewModel.deleteCategory(
                                    categoryId.toString(),
                                    onResult = { success ->
                                        if (success) {
                                            navController.navigate(Screens.StartScreen.route)
                                        }
                                    })
                            })
                    }

                    ChatGptButton(onResponse = { response ->
                        // Parse the response and update the form fields
                        val parsedCategory = parseCategoryResponse(extractJsonContent(response))
                        categoryName = parsedCategory.name
                        categoryQuestions = parsedCategory.questionList
                    })

                    QuizButton(
                        text = stringResource(id = R.string.add_question),
                        onClick = {
                            categoryQuestions =
                                categoryQuestions + Question("", listOf(), userAdded = true)
                        }
                    )

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

                    if (isEdit) {
                        QuizButton(
                            text = stringResource(id = R.string.update_category),
                            onClick = {
                                if (categoryName.isNotBlank() && categoryQuestions.isNotEmpty() && categoryQuestions.all
                                    { question ->
                                        question.answers.isNotEmpty() && question.answers.all { answer ->
                                            answer.answer.isNotBlank()
                                        }
                                    }
                                ) {
                                    viewModel.updateCategory(
                                        categoryId.toString(),
                                        categoryName,
                                        categoryQuestions,
                                        onResult = { success ->
                                            errorMessage =
                                                if (success) "" else R.string.failed_to_update_category.toString()
                                            if (success.not()) return@updateCategory
                                            val route =
                                                Screens.NewCategoryConfirmationScreen.route + "/$categoryName"
                                            navController.navigate(route)
                                        })
                                } else {
                                    categoryIsEmpty =
                                        checkCategories(categoryName, categoryQuestions)
                                    errorMessage = R.string.invalid_input.toString()
                                }
                            })
                    } else {
                        QuizButton(text = stringResource(id = R.string.send_category), onClick = {
                            if (categoryName.isNotBlank() && categoryQuestions.isNotEmpty() && categoryQuestions.all
                                { question ->
                                    question.answers.isNotEmpty() && question.answers.all { answer ->
                                        answer.answer.isNotBlank()
                                    }
                                }
                            ) {
                                viewModel.addNewCategory(
                                    categoryName,
                                    categoryQuestions,
                                    onResult = { success ->
                                        errorMessage =
                                            if (success) "" else R.string.category_name_occupied.toString()
                                        if (success.not()) return@addNewCategory
                                        val route =
                                            Screens.NewCategoryConfirmationScreen.route + "/$categoryName"
                                        navController.navigate(route)
                                    })
                            } else {
                                categoryIsEmpty = checkCategories(categoryName, categoryQuestions)
                                errorMessage = R.string.invalid_input.toString()
                            }
                        })
                    }

                    if (errorMessage.isNotEmpty()) {
                        QuizText(
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
    }
}

fun extractJsonContent(response: String): String {
    val jsonStartIndex = response.indexOf("{")
    val jsonEndIndex = response.lastIndexOf("}") + 1
    return if (jsonStartIndex != -1 && jsonEndIndex != -1) {
        response.substring(jsonStartIndex, jsonEndIndex)
    } else {
        ""
    }
}

fun parseCategoryResponse(response: String): Category {
    val jsonContent = extractJsonContent(response)
    if (jsonContent.isBlank()) {
        throw IllegalArgumentException("Invalid JSON content")
    }

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(CategoryDto::class.java)
    val categoryDto = jsonAdapter.fromJson(jsonContent)

    return categoryDto?.toCategory() ?: throw IllegalArgumentException("Failed to parse JSON")
}

@Composable
fun QuestionPanel(
    question: Question,
    questionIndex: Int,
    onQuestionChange: (Question) -> Unit,
    onRemoveQuestion: () -> Unit,
    categoryIsEmpty: CategoryCheck
){
    val buttonColor = Color(0xFF0077b6)

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
            label = { QuizText(text = stringResource(id = R.string.question_name)) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                val newAnswer = Answer("", false, userAdded = true)
                answers = answers + newAnswer
                onQuestionChange(question.copy(name = questionName, answers = answers))
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("+", color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { showAnswers = !showAnswers },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(if (showAnswers) "^" else "v", color = Color.White)
        }
        Button(
            onClick = onRemoveQuestion,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("-", color = Color.White)
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
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF0077b6) // Set your desired color here
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        answers = answers.toMutableList().apply {
                            removeAt(index)
                        }
                        onQuestionChange(question.copy(name = questionName, answers = answers))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("-", color = Color.White)
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