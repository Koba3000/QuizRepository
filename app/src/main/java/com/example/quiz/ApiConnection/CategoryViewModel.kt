package com.example.quiz.ApiConnection

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

//    private fun fetchCategories() {
//        viewModelScope.launch {
//            try {
//                categories = repository.getCategories()
//
//                Log.d("CategoryViewModel", "Categories fetched successfully")
//            } catch (e: Exception) {
//                Log.e("CategoryViewModel", "Failed to fetch categories", e)
//            }
//            isLoading = false
//        }
//    }
}