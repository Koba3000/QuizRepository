package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.quiz.ApiConnection.CategoryViewModel
import com.example.quiz.screens.loadLanguagePreference
import com.example.quiz.screens.setLocale
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