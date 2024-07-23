package com.example.quiz.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel(){
    val languages = listOf("en", "pl", "fr")
    var selectedLanguage = mutableStateOf(languages[0])
}