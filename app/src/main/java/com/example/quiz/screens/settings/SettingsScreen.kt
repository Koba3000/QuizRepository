package com.example.quiz.screens.settings

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.quiz.view.Screens
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }
    val languages = settingsViewModel.languages
    val selectedLanguage = settingsViewModel.selectedLanguage.value
    var user by loginViewModel.user

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QUIZ APP SETTINGS") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.StartScreen.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(text = stringResource(R.string.settings_test), fontSize = 24.sp)

//            // Button for English
//            Button(onClick = {
//                isLoading.value = true
//                updateLocale(context, "en", isLoading)
//            }) {
//                Text("English")
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Button for Français
//            Button(onClick = {
//                isLoading.value = true
//                updateLocale(context, "fr", isLoading)
//            }) {
//                Text("Français")
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Button for Polski
//            Button(onClick = {
//                isLoading.value = true
//                updateLocale(context, "pl", isLoading)
//            }) {
//                Text("Polski")
//            }


            Log.d("SettingsScreen", "Selected Language: $selectedLanguage")

            DropdownMenu(
                items = languages,
                selectedItem = selectedLanguage,
                label = "Select language",
                onSelect = { selected ->
                    if (selected != null) {
                        settingsViewModel.selectedLanguage.value = selected
                        isLoading.value = true
                        Log.d("SettingsScreen", "Selected Language: $selected")
                        updateLocale(context, selected, isLoading)
                    }
                }
            )

            Text((stringResource(R.string.welcome_user, user?.displayName ?: "")))
            AsyncImage(
                model = user?.photoUrl,
                contentDescription = "User Photo",
                modifier = Modifier.size(64.dp)
            )

            Button(onClick = {
                loginViewModel.logout()
                navController.navigate(Screens.LoginScreen.route)
            }) {
                Text(stringResource(id = R.string.logout))
            }

        }
    }
}

fun updateLocale(context: Context, languageCode: String, isLoading: MutableState<Boolean>) {
    saveLanguagePreference(context, languageCode)

    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    if (context is Activity) {
        context.runOnUiThread {
            context.recreate()
            isLoading.value = false
        }
    }
}

fun saveLanguagePreference(context: Context, languageCode: String) {
    val prefs = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE)
    prefs.edit().putString("SelectedLanguage", languageCode).apply()
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
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}