package com.example.quiz.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.quiz.R
import com.example.quiz.screens.login.LoginViewModel
import com.example.quiz.screens.start.composables.ChatGptButton
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val user = loginViewModel.user.value
    val backgroundColor = Color(0xFFcaf0f8)
    val topBarBackgroundColor = Color(0xFF90e0ef)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBackgroundColor
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        QuizText(
                            text = stringResource(id = R.string.app_name),
                            fontSize = FontSizeLarge
                        )
                    }
                },
                actions = {
                    user?.photoUrl?.let { avatarUrl ->
                        Box(
                            modifier = Modifier.padding(25.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(avatarUrl),
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .clickable { navController.navigate(Screens.SettingsScreen.route) }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor) // Replace with your desired background color
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
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
                ChatGptButton()
            }
        }
    }
}

@Composable
fun NavigationButton(
    navController: NavController,
    route: String,
    text: String
){
    QuizButton(
        text = text,
        onClick = { navController.navigate(route) }
    )
}
