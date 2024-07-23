package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quiz.screens.settings.loadLanguagePreference
import com.example.quiz.screens.settings.setLocale
import com.example.quiz.view.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load and apply the saved language preference
        loadLanguagePreference(this)?.let { setLocale(this, it) }
        setContent {
            Navigation()
        }
    }
}