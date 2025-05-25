package com.example.gamediary.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.example.gamediary.model.StyleRange

/**
 * Builds an AnnotatedString from the given
 * @param text
 * and a list of
 * @param [StyleRange] objects.
 * This function iterates through the text and applies the styles from the provided ranges.
 */
fun buildAnnotatedStringHelper(
    text: String,
    styleRanges: List<StyleRange>
): AnnotatedString {
    return buildAnnotatedString {
        append(text)
        // Apply styles from the list of StyleRange objects
        styleRanges.forEach { styleRange ->
            // Ensure the range is valid within the current text length
            if (styleRange.start < text.length && styleRange.end <= text.length && styleRange.start < styleRange.end) {
                addStyle(styleRange.toSpanStyle(), styleRange.start, styleRange.end)
            }
        }
    }
}