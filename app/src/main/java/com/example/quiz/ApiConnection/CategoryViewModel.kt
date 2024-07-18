package com.example.quiz.ApiConnection

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.model.Category
import com.example.quiz.model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    var isLoading by mutableStateOf(true)
    var categories by mutableStateOf(emptyList<CategoryDto>())
    var answerStates = mutableStateOf(listOf<List<MutableState<Boolean>>>())

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    categories = repository.getCategories()
                    // Initialize answerStates based on fetched categories
                    answerStates.value = categories.flatMap { category ->
                        category.questions?.map { question ->
                            question.answers?.map { mutableStateOf(false) } ?: emptyList()
                        } ?: emptyList()
                    }
                    Log.d("CategoryViewModel", "Categories fetched successfully")
                } catch (e: Exception) {
                    Log.e("CategoryViewModel", "Failed to fetch categories", e)
                }
                isLoading = false
            }
        }
    }

    fun addNewCategory(categoryName: String, questions: List<Question>, onResult:(Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Convert List<Question> to List<QuestionDto>
                val questionDtos = questions.map { question ->
                    QuestionDto(
                        name = question.name,
                        answers = question.answers.map { answer ->
                            AnswerDto(answer = answer.answer, isCorrect = answer.isCorrect)
                        }
                    )
                }
                // Create CategoryDto
                val categoryDto = CategoryDto(name = categoryName, questions = questionDtos)
                // Use repository to send the CategoryDto
                repository.createCategory(categoryDto)
                // Handle success (e.g., update UI state)
                Log.d("CategoryViewModel", "Category created successfully")
                onResult(true)
                return@launch
            } catch (e: HttpException) {
                if (e.code() == 409) {
                    // Handle specific 409 Conflict error
                    Log.e("CategoryViewModel", "Conflict: Category already exists", e)
                } else {
                    // Handle other HTTP errors
                    Log.e("CategoryViewModel", "Failed to create category", e)
                }
            } catch (e: Exception) {
                // Handle non-HTTP errors
                Log.e("CategoryViewModel", "Failed to create category", e)
            }
            onResult(false)
        }
    }

    fun updateCategory(categoryId: String, categoryName: String, questions: List<Question>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Convert List<Question> to List<QuestionDto>
                val questionDtos = questions.map { question ->
                    QuestionDto(
                        name = question.name,
                        answers = question.answers.map { answer ->
                            AnswerDto(answer = answer.answer, isCorrect = answer.isCorrect)
                        }
                    )
                }
                // Create CategoryDto
                val categoryDto = CategoryDto(name = categoryName, questions = questionDtos)
                // Use Retrofit to send the PUT request
                val response = repository.updateCategory(categoryId, categoryDto)
                if (response.isSuccessful) {
                    // Handle success (e.g., update UI state)
                    Log.d("CategoryViewModel", "Category updated successfully")

                    onResult(true)
                } else {
                    // Handle request failure
                    Log.e("CategoryViewModel", "Failed to update category")
                    onResult(false)
                }
            } catch (e: Exception) {
                // Handle other errors
                Log.e("CategoryViewModel", "Failed to update category", e)
                onResult(false)
            }
        }
    }
}