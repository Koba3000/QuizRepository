package com.example.quiz.ApiConnection

import android.util.Log
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

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                categories = repository.getCategories()

                Log.d("CategoryViewModel", "Categories fetched successfully")
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Failed to fetch categories", e)
            }
            isLoading = false
        }
    }
}