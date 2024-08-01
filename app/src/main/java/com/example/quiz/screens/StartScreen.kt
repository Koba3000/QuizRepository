package com.example.quiz.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quiz.R
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { QuizText(stringResource(id = R.string.app_name)) }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondary // Change to your desired color
            ) {
                // Spacer to push the settings icon to the right
                Spacer(modifier = Modifier.weight(1f))
                // Settings icon button
                IconButton(
                    onClick = { navController.navigate(Screens.SettingsScreen.route) },
                    modifier = Modifier.padding(25.dp)
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationButton(
                navController = navController,
                route = Screens.CategoryScreen.route,
                text = stringResource(id = R.string.play_quiz)
            )
            NavigationButton(
                navController = navController,
                route = Screens.CategoriesToEdit.route,
                text = stringResource(id = R.string.categories)
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
