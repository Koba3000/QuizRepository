package com.example.quiz.screens.settings

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    val languages = listOf("en", "pl", "fr")
    var selectedLanguage = mutableStateOf(languages[0])

    init {
        viewModelScope.launch {
            val savedLanguage = loadLanguagePreference()
            if (savedLanguage != null && languages.contains(savedLanguage)) {
                selectedLanguage.value = savedLanguage
            }
        }
    }

    fun updateSelectedLanguage(language: String) {
        selectedLanguage.value = language
        saveLanguagePreference(language)
    }

    private fun saveLanguagePreference(languageCode: String) {
        val prefs = getApplication<Application>().getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE)
        prefs.edit().putString("SelectedLanguage", languageCode).apply()
    }

    private fun loadLanguagePreference(): String? {
        val prefs = getApplication<Application>().getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE)
        return prefs.getString("SelectedLanguage", "en")
    }
}