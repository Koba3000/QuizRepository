package com.example.quiz.ApiConnection

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("category/")
    suspend fun createCategory(@Body category: CategoryDto): CategoryDto

    @GET("category/")
    suspend fun getCategories(): List<CategoryDto>
}