package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController
){
    CategoryButtons()
}


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