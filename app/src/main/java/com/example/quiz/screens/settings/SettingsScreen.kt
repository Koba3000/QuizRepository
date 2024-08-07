package com.example.quiz.screens.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quiz.R
import com.example.quiz.screens.settings.composables.DropdownMenu
import com.example.quiz.screens.login.LoginViewModel
import com.example.quiz.ui.theme.FontSizeLarge
import com.example.quiz.ui.theme.QuizButton
import com.example.quiz.ui.theme.QuizText
import com.example.quiz.view.Screens
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }
    val languages = settingsViewModel.languages
    val selectedLanguage = settingsViewModel.selectedLanguage.value
    val user by loginViewModel.user
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
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QuizText(
                    text = stringResource(R.string.settings_test),
                    fontSize = FontSizeLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DropdownMenu(
                    items = languages,
                    selectedItem = selectedLanguage,
                    label = stringResource(id = R.string.select_language),
                    onSelect = { selected ->
                        if (selected != null) {
                            isLoading.value = true
                            settingsViewModel.updateSelectedLanguage(selected)
                            updateLocale(context, selected, isLoading)
                        }
                    }
                )

                QuizText(
                    text = stringResource(R.string.welcome_user, user?.displayName ?: ""),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AsyncImage(
                    model = user?.photoUrl,
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp)
                )

                QuizButton(
                    onClick = {
                        loginViewModel.logout()
                        navController.navigate(Screens.LoginScreen.route)
                    },
                    text = stringResource(id = R.string.logout)
                )
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(backgroundColor)
//                .verticalScroll(scrollState)
//        ) {
//            Text(text = stringResource(R.string.settings_test), fontSize = 24.sp)
//
//            DropdownMenu(
//                items = languages,
//                selectedItem = selectedLanguage,
//                label = stringResource(id = R.string.select_language),
//                onSelect = { selected ->
//                    if (selected != null) {
//                        isLoading.value = true
//                        settingsViewModel.updateSelectedLanguage(selected)
//                        updateLocale(context, selected, isLoading)
//                    }
//                }
//            )
//
//            Text((stringResource(R.string.welcome_user, user?.displayName ?: "")))
//            AsyncImage(
//                model = user?.photoUrl,
//                contentDescription = "User Photo",
//                modifier = Modifier.size(64.dp)
//            )
//
//            Button(onClick = {
//                loginViewModel.logout()
//                navController.navigate(Screens.LoginScreen.route)
//            }) {
//                Text(stringResource(id = R.string.logout))
//            }
//
//        }
    }
}

fun updateLocale(context: Context, languageCode: String, isLoading: MutableState<Boolean>) {
    // Save the language preference
    saveLanguagePreference(context, languageCode)

    // Update the locale
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Recreate the Activity to apply the new locale
    if (context is Activity) {
        context.runOnUiThread {
            context.recreate()
            isLoading.value = false
        }
    }
}

fun saveLanguagePreference(context: Context, languageCode: String) {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("language_code", languageCode)
        apply()
    }
}

fun loadLanguagePreference(context: Context): String?{
    val prefs = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE)
    return prefs.getString("SelectedLanguage", "en")
}

fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.createConfigurationContext(config)
}