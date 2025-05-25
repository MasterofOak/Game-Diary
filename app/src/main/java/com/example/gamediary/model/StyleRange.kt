package com.example.gamediary.model

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.serialization.Serializable

@Serializable
data class StyleRange(
    val start: Int,
    val end: Int,
    val fontWeight: String? = null,
    val fontStyle: String? = null,
    val textDecoration: String? = null
) {
    
    fun toSpanStyle(): SpanStyle {
        val fontWeight = this.fontWeight?.let { weight ->
            when (weight) {
                "FontWeight(weight=400)" -> FontWeight.Normal
                "FontWeight(weight=700)" -> FontWeight.Bold
                else -> null
            }
        }
        val fontStyle = this.fontStyle?.let { style ->
            when (style) {
                "Normal" -> FontStyle.Normal
                "Italic" -> FontStyle.Italic
                else -> null
            }
        }
        val textDecoration = this.textDecoration?.let { decoration ->
            when (decoration) {
                "TextDecoration.None" -> TextDecoration.None
                "TextDecoration.Underline" -> TextDecoration.Underline
                else -> null
            }
        }
        return SpanStyle(
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textDecoration = textDecoration
        )
    }
}
