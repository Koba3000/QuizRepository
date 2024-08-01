package com.example.quiz.ui.theme


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun QuizText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextColorPrimary,
    fontSize: TextUnit = FontSizeMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily = CustomFontFamily,
    style: TextStyle = TextStyle.Default
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        style = style,
        textAlign = TextAlign.Center
    )
}