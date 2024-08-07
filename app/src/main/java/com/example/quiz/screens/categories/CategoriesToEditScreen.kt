package com.example.quiz.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quiz.R
import com.example.quiz.screens.start.NavigationButton
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesToEditScreen (
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val scrollState = rememberScrollState()
    val topBarBackgroundColor = Color(0xFF90e0ef)
    val backgroundColor = Color(0xFFcaf0f8)

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
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBarBackgroundColor) // Replace with your desired background color
                    .padding(8.dp)
            ) {
                NavigationButton(
                    navController = navController,
                    route = Screens.CategoryForms.route,
                    text = stringResource(id = R.string.add_category)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
                viewModel.categories.forEach {
                    EditCategoryNavigationButton(
                        navController = navController,
                        text = it.name,
                        buttonId = it.id.toString()
                    )
                }
            }
        }
//        Column (
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(16.dp)
//                .verticalScroll(scrollState)
//        ) {
//            if (viewModel.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//            viewModel.categories.forEach {
//                EditCategoryNavigationButton(
//                    navController = navController,
//                    text = it.name,
//                    buttonId = it.id.toString()
//                )
//            }
//        }
    }
}

@Composable
fun EditCategoryNavigationButton(navController: NavController, text: String, buttonId: String) {
    val url = Screens.CategoryForms.route.replace("{categoryId}", buttonId)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
//        Button(onClick = { navController.navigate(url) }) {
//            Text(text = text, style = TextStyle(fontSize = 20.sp))
//        }
        QuizButton(text = text, onClick = {navController.navigate(url)})
    }
}