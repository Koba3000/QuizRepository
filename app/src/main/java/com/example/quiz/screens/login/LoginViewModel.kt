package com.example.quiz.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var user = mutableStateOf<FirebaseUser?>(Firebase.auth.currentUser)
    private set

    fun logout() {
        Firebase.auth.signOut()
        user.value = null
    }

    fun setUser(newUser: FirebaseUser?) {
        user.value = newUser
    }

    fun isUserLoggedIn(): Boolean {
        return user.value != null
    }
}