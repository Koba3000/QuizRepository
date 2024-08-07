package com.example.quiz.apiConnection

import retrofit2.Response

class CategoryRepository(private val apiService: ApiService): ApiService {

    override suspend fun createCategory(category: CategoryDto): CategoryDto {
        return apiService.createCategory(category)
    }

    override suspend fun getCategories(): List<CategoryDto> {
        return apiService.getCategories()
    }

    override suspend fun updateCategory(categoryId: String, categoryDto: CategoryDto): Response<CategoryDto> {
        return apiService.updateCategory(categoryId, categoryDto)
    }

    override suspend fun deleteCategory(categoryId: String): Response<Unit> {
        return apiService.deleteCategory(categoryId)
    }
}
