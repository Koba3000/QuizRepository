package com.example.quiz.ApiConnection

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("category/")
    suspend fun createCategory(@Body category: CategoryDto): CategoryDto

    @GET("category/")
    suspend fun getCategories(): List<CategoryDto>

    @PUT("category/{id}")
    suspend fun updateCategory(@Path("id") categoryId: String, @Body categoryDto: CategoryDto): Response<CategoryDto>

    @DELETE("category/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: String): Response<Unit>
}