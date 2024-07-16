package com.example.quiz.ApiConnection

class CategoryRepository(private val apiService: ApiService): ApiService {

    override suspend fun createCategory(category: CategoryDto): CategoryDto {
        return apiService.createCategory(category)
    }

    override suspend fun getCategories(): List<CategoryDto> {
        return apiService.getCategories()
    }
}
