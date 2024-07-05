package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QUIZ APP") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationButton(
                navController = navController,
                route = Screens.CategoryScreen.route,
                text = "play quiz app"
            )
        }
    }
}

@Composable
fun NavigationButton(
    navController: NavController,
    route: String,
    text: String
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp),
        onClick = {
        navController.navigate(route)
    }) {
        Text(
            text = text,
            style = TextStyle(fontSize = 25.sp)
        )
    }
}
