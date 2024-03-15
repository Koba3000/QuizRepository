package com.example.quiz

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiz.view.Screens

@Composable
fun StartScreen(
    navController: NavController
) {
    NavigationButton(navController = navController, "let's play quiz")
}

@Composable
fun NavigationButton(navController: NavController, text: String) {
    Button(onClick = {
        navController.navigate(Screens.CategoryScreen.route)
    }) {
        Text(text = text)
    }
}


//
@Composable
fun SimpleButton(text: String){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp),
        onClick = {
            Log.d("SimpleButton", "Button Clicked")
        },
        shape = RectangleShape
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun SimpleButtonPreview(){
    SimpleButton("Simple button")
}

