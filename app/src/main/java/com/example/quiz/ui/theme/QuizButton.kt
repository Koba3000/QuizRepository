package com.example.quiz.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp),
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0077b6)
        ),
        modifier = modifier,
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}