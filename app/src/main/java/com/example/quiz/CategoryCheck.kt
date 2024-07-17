package com.example.quiz

data class CategoryCheck(
    val isCategoryNameEmpty: Boolean,
    val emptyQuestionsIndexes: List<Int>,
    val emptyAnswersIndexes: Map<Int, List<Int>> // Key: Question Index, Value: List of Answer Indexes
)