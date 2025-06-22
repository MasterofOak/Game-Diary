package com.masterofoak.gamediary.ui.add_records_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatClear
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.masterofoak.gamediary.model.StyleRange
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.utils.buildAnnotatedStringHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.math.max
import kotlin.math.min


@Composable
fun AddTextRecord(
    recordToUpdate: TextRecord?,
    updateState: (String, List<StyleRange>) -> Unit,
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                recordToUpdate?.content ?: "", selection = TextRange(0)
            )
        )
    }
    var styleRanges by remember {
        if (recordToUpdate != null) {
            return@remember mutableStateOf(Json.decodeFromString<List<StyleRange>>(recordToUpdate.styleRanges))
        } else {
            return@remember mutableStateOf(listOf<StyleRange>())
        }
    }
    var textFieldJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    Column {
        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldJob?.cancel()
                val oldText = textFieldValue.text
                val newText = newValue.text
                val diff = newText.length - oldText.length
                val changeStart = newValue.selection.start // The cursor position is often the start of the change
                
                // Update the list of style ranges based on the text change
                styleRanges = adjustStyleRangesOnTextChange(styleRanges, changeStart, diff)
                
                // Update the text field value
                
                // Rebuild the annotated string based on the updated text and style ranges
                textFieldValue = newValue
                val newAnnotatedString = buildAnnotatedStringHelper(textFieldValue.text, styleRanges)
                textFieldValue = textFieldValue.copy(annotatedString = newAnnotatedString)
                textFieldJob = coroutineScope.launch {
                    delay(500L)
                    updateState(textFieldValue.text, styleRanges)
                    println("State updated!")
                }
            },
            placeholder = { Text("Your Text") },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.size(8.dp))
        FormattingButtons(
            textFieldValue = textFieldValue,
            styleRanges = styleRanges,
            updateStyleRangesList = { styleRanges = it },
            updateTextFieldValue = { textFieldValue = it })
    }
}

@Composable
fun FormattingButtons(
    textFieldValue: TextFieldValue,
    styleRanges: List<StyleRange>,
    updateStyleRangesList: (List<StyleRange>) -> Unit,
    updateTextFieldValue: (TextFieldValue) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(
            onClick = {
                makeTextBold(
                    textFieldValue,
                    styleRanges,
                    updateStyleRangesList = updateStyleRangesList,
                    updateTextFieldValue = updateTextFieldValue
                )
            }, modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatBold,
                contentDescription = "Make text bold",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        }
        IconButton(
            onClick = {
                makeTextItalic(
                    textFieldValue,
                    styleRanges,
                    updateStyleRangesList = updateStyleRangesList,
                    updateTextFieldValue = updateTextFieldValue
                )
            }, modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatItalic,
                contentDescription = "Make text italic",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        }
        IconButton(
            onClick = {
                makeTextUnderlined(
                    textFieldValue,
                    styleRanges,
                    updateStyleRangesList = updateStyleRangesList,
                    updateTextFieldValue = updateTextFieldValue
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
                .size(36.dp)
        
        ) {
            Icon(
                imageVector = Icons.Default.FormatUnderlined,
                contentDescription = "Make text underlined",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            
            )
        }
        IconButton(
            onClick = { clearFormatting() },
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatClear,
                contentDescription = "Clear formatting",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            
            )
        }
    }
}

private fun makeTextBold(
    textFieldValue: TextFieldValue,
    styleRanges: List<StyleRange>,
    updateStyleRangesList: (List<StyleRange>) -> Unit,
    updateTextFieldValue: (TextFieldValue) -> Unit
) {
    if (textFieldValue.selection.start != textFieldValue.selection.end) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end
        // Define the style to toggle (Bold)
        val newStyle = SpanStyle(fontWeight = FontWeight.Bold)
        
        // Get the desired final set of ranges after applying the style change
        // This function now operates on and returns a List<StyleRange>
        val updateStyleRanges = getUpdatedStyleRanges(
            currentStyleRanges = styleRanges,
            selectionStart = start,
            selectionEnd = end,
            newStyle = newStyle
        )
        updateStyleRangesList(updateStyleRanges)
        val newAnnotatedString = buildAnnotatedStringHelper(textFieldValue.text, updateStyleRanges)
        updateTextFieldValue(textFieldValue.copy(annotatedString = newAnnotatedString))
    }
}

private fun makeTextItalic(
    textFieldValue: TextFieldValue,
    styleRanges: List<StyleRange>,
    updateStyleRangesList: (List<StyleRange>) -> Unit,
    updateTextFieldValue: (TextFieldValue) -> Unit
) {
    if (textFieldValue.selection.start != textFieldValue.selection.end) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end
        // Define the style to toggle (Italic)
        val newStyle = SpanStyle(fontStyle = FontStyle.Italic)
        
        // Get the desired final set of ranges after applying the style change
        // This function now operates on and returns a List<StyleRange>
        val updateStyleRanges = getUpdatedStyleRanges(
            currentStyleRanges = styleRanges,
            selectionStart = start,
            selectionEnd = end,
            newStyle = newStyle
        )
        updateStyleRangesList(updateStyleRanges)
        val newAnnotatedString = buildAnnotatedStringHelper(textFieldValue.text, updateStyleRanges)
        updateTextFieldValue(textFieldValue.copy(annotatedString = newAnnotatedString))
    }
}

private fun makeTextUnderlined(
    textFieldValue: TextFieldValue,
    styleRanges: List<StyleRange>,
    updateStyleRangesList: (List<StyleRange>) -> Unit,
    updateTextFieldValue: (TextFieldValue) -> Unit
) {
    if (textFieldValue.selection.start != textFieldValue.selection.end) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end
        // Define the style to toggle (Underlined)
        val newStyle = SpanStyle(textDecoration = TextDecoration.Underline)
        
        // Get the desired final set of ranges after applying the style change
        // This function now operates on and returns a List<StyleRange>
        val updateStyleRanges = getUpdatedStyleRanges(
            currentStyleRanges = styleRanges,
            selectionStart = start,
            selectionEnd = end,
            newStyle = newStyle
        )
        updateStyleRangesList(updateStyleRanges)
        val newAnnotatedString = buildAnnotatedStringHelper(textFieldValue.text, updateStyleRanges)
        updateTextFieldValue(textFieldValue.copy(annotatedString = newAnnotatedString))
    }
}

private fun clearFormatting() {

}

/**
 * Adjusts the start and end indices of existing style ranges when the text changes.
 * This handles insertions and deletions by shifting or removing ranges as needed.
 */
private fun adjustStyleRangesOnTextChange(
    currentStyleRanges: List<StyleRange>,
    changeStart: Int, // The index where the text change occurred
    diff: Int // The difference in text length (positive for insertion, negative for deletion)
): List<StyleRange> {
    val newStyleRanges = mutableListOf<StyleRange>()
    
    // Iterate through the current style ranges
    for (styleRange in currentStyleRanges) {
        var newStart = styleRange.start
        var newEnd = styleRange.end
        
        // Case 1: Change is before the range - shift the range
        if (changeStart <= styleRange.start) {
            newStart = styleRange.start + diff
            newEnd = styleRange.end + diff
        }
        // Case 2: Change is within the range
        else if (changeStart > styleRange.start && changeStart < styleRange.end) {
            // If deleting (diff is negative)
            newEnd = if (diff < 0) {
                // If the deletion extends beyond the range end, the new end is the change start
                if (changeStart - diff >= styleRange.end) changeStart else styleRange.end + diff
            }
            // If inserting (diff is positive)
            else {
                // Shift the end of the range
                styleRange.end + diff
            }
        }
        // Case 3: Change is after the range - no change to this range's indices
        // The indices are already correct relative to the start of the text
        
        // Only keep ranges that are still valid (start < end) after adjustment
        if (newStart < newEnd) {
            newStyleRanges.add(
                StyleRange(
                    newStart,
                    newEnd,
                    styleRange.fontWeight,
                    styleRange.fontStyle,
                    styleRange.textDecoration
                )
            )
        }
    }
    
    return newStyleRanges
}


/**
 * Calculates the updated list of StyleRanges after applying or removing a style
 * within a given selection range. This function determines the final styling
 * across the entire text based on the current styles and the requested change.
 * It returns the complete list of StyleRange objects.
 */
private fun getUpdatedStyleRanges(
    currentStyleRanges: List<StyleRange>, // The current list of style ranges
    selectionStart: Int, // The start index of the selection
    selectionEnd: Int, // The end index of the selection
    newStyle: SpanStyle // The style to toggle (contains either fontWeight or fontStyle)
): List<StyleRange> {
    val finalStyleRanges = mutableListOf<StyleRange>()
    
    // Collect all unique boundary points from existing ranges and the selection boundaries.
    // These points define the segments where the style might change.
    val stylePointsSet = mutableSetOf(0, selectionStart, selectionEnd)
    currentStyleRanges.forEach { range ->
        stylePointsSet.add(range.start)
        stylePointsSet.add(range.end)
    }
    // Sort the boundary points and filter out negative values
    val sortedStylePointsSet = stylePointsSet.sorted().filter { it >= 0 }
    
    // Process segments defined by the sorted boundary points across the entire text.
    // We iterate through consecutive pairs of boundary points to define each segment.
    for (i in 0 until sortedStylePointsSet.size - 1) {
        val styleStartPoint = sortedStylePointsSet[i]
        val styleEndPoint = sortedStylePointsSet[i + 1]
        
        // Only process valid segments (where the start is before the end)
        if (styleStartPoint < styleEndPoint) {
            // Determine the current combined style for this segment
            var currentCombinedStyle = SpanStyle()
            currentStyleRanges.forEach { styleRange ->
                val rangeStyle = styleRange.toSpanStyle()
                // If the range overlaps with the current segment, merge its style
                if (max(styleRange.start, styleStartPoint) < min(styleRange.end, styleEndPoint)) {
                    currentCombinedStyle = currentCombinedStyle.merge(rangeStyle)
                }
            }
            
            // Determine the target style for this segment.
            var targetStyle = currentCombinedStyle
            
            // If the segment overlaps with the selected range, apply the toggle logic.
            if (styleStartPoint < selectionEnd && styleEndPoint > selectionStart) {
                targetStyle = toggleStyle(currentCombinedStyle, newStyle)
            }
            
            // Add the StyleRange for this segment to the final list.
            // We only add ranges that have some style applied, or if they are within the selected range
            // (even if the style becomes normal, we need to represent that segment).
            if (targetStyle != SpanStyle() || (styleStartPoint < selectionEnd && styleEndPoint > selectionStart)) {
                // Check if the last added range can be merged with the current one.
                // This helps to consolidate adjacent segments with the same final style.
                if (finalStyleRanges.isNotEmpty() &&
                    finalStyleRanges.last().end == styleStartPoint &&
                    finalStyleRanges.last().toSpanStyle() == targetStyle
                ) {
                    // Merge with the previous range by extending its end point
                    val lastRange = finalStyleRanges.removeAt(finalStyleRanges.lastIndex)
                    finalStyleRanges.add(
                        StyleRange(
                            lastRange.start, styleEndPoint, targetStyle.fontWeight.toString(),
                            targetStyle.fontStyle.toString(), targetStyle.textDecoration.toString()
                        )
                    )
                } else {
                    // Add as a new range
                    finalStyleRanges.add(
                        StyleRange(
                            styleStartPoint, styleEndPoint, targetStyle.fontWeight.toString(),
                            targetStyle.fontStyle.toString(), targetStyle.textDecoration.toString()
                        )
                    )
                }
            }
        }
    }
    
    // Return the calculated list of final StyleRange objects.
    return finalStyleRanges
}

/**
 * Helper function to toggle a specific style property (fontWeight or fontStyle)
 * within an existing SpanStyle. If the property is already present and matches
 * the new style, it's removed (set to Normal). Otherwise, it's applied.
 */
private fun toggleStyle(currentStyle: SpanStyle, newStyle: SpanStyle): SpanStyle {
    var targetFontWeight = currentStyle.fontWeight
    var targetFontStyle = currentStyle.fontStyle
    var targetTextDecoration = currentStyle.textDecoration
    
    // Toggle fontWeight if newStyle provides it
    if (newStyle.fontWeight != null) {
        targetFontWeight = if (currentStyle.fontWeight == newStyle.fontWeight) {
            FontWeight.Normal // Toggle off if already applied (e.g., Bold -> Normal)
        } else {
            newStyle.fontWeight // Apply if not applied or different (e.g., Normal -> Bold)
        }
    }
    
    // Toggle fontStyle if newStyle provides it
    if (newStyle.fontStyle != null) {
        targetFontStyle = if (currentStyle.fontStyle == newStyle.fontStyle) {
            FontStyle.Normal // Toggle off if already applied (e.g., Italic -> Normal)
        } else {
            newStyle.fontStyle // Apply if not applied or different (e.g., Normal -> Italic)
        }
    }
    if (newStyle.textDecoration != null) {
        targetTextDecoration = if (currentStyle.textDecoration == newStyle.textDecoration) {
            TextDecoration.None // Toggle off if already applied (e.g., Italic -> Normal)
        } else {
            newStyle.textDecoration // Apply if not applied or different (e.g., Normal -> Italic)
        }
    }
    
    // Return a new SpanStyle with the toggled properties, preserving other properties
    return currentStyle.copy(
        fontWeight = targetFontWeight,
        fontStyle = targetFontStyle,
        textDecoration = targetTextDecoration
    )
}
