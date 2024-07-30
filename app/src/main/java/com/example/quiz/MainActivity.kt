package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quiz.screens.settings.loadLanguagePreference
import com.example.quiz.screens.settings.setLocale
import com.example.quiz.view.Navigation
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadLanguagePreference(this)?.let { setLocale(this, it) }

        setContent {
            Navigation()
        }
    }
}