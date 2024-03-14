package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CategoryButtons(){
    Column {
        SimpleButton("Simple button")
        SimpleButton("Simple button")
        SimpleButton("Simple button")
    }
}

@Preview
@Composable
fun CategoryButtonsPreview(){
    Column {
        SimpleButton("Simple button")
        SimpleButton("Simple button")
        SimpleButton("Simple button")
    }
}