package com.example.gamediary.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamediary.GlobalViewModelProvider
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.ui.viewmodel.AddGameViewModel
import com.example.gamediary.ui.viewmodel.FullTag
import com.example.gamediary.utils.decodeBitmapFromUri

@Composable
fun AddGameScreen(
    viewModel: AddGameViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val uiState = viewModel.uiState.collectAsState().value
    var gameNameTextField by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri -> imageUri = uri }
    imageUri?.let { LocalContext.current.contentResolver.takePersistableUriPermission(it, flag) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GameImage(photoPicker, imageUri)
            TextField(
                value = gameNameTextField,
                onValueChange = { gameNameTextField = it },
                label = { Text("Game Name:") },
                placeholder = { Text("Half-Life 2") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = gameNameTextField.isNotEmpty() && gameNameTextField.length < 2,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            )
        }
        Tags(
            tagsList = uiState.tagsList,
            selectedTags = uiState.selectedTags,
            addTagToList = viewModel::addSelectedTagToList
        )
        Button(onClick = {
            viewModel.addGame(gameNameTextField, imageUri.toString())
            navigateUp()
            viewModel.clearSelectedTags()
        }) {
            Text("Add Game")
        }
    }
}

@Composable
private fun GameImage(
    photoPicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    imageUri: Uri?,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(4.dp, Color.DarkGray, CircleShape)
                .background(Color.LightGray)
                .clickable { photoPicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri == null) {
                Image(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
            } else {
                val context = LocalContext.current
                Image(
                    bitmap = decodeBitmapFromUri(context, imageUri)!!,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(4.dp, Color.Red, CircleShape)
                
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text("Game Image")
    }
}

@Composable
private fun Tags(
    tagsList: List<FullTag>,
    selectedTags: List<Int>,
    addTagToList: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isTagsClicked by remember { mutableStateOf(false) }
    val tagIconRotation by animateFloatAsState(targetValue = if (isTagsClicked) 225f else 0f, label = "tagIconRotation")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedAssistChip(
            onClick = { isTagsClicked = !isTagsClicked },
            label = { Text("Tags") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(tagIconRotation)
                )
            },
            shape = RoundedCornerShape(4.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(":")
        Spacer(modifier = Modifier.size(4.dp))
        AnimatedVisibility(isTagsClicked, enter = expandHorizontally(), exit = shrinkHorizontally()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                    .fillMaxWidth(if (isTagsClicked) 1f else 0f)
            ) {
                items(tagsList, key = { it.tagId }) { tag ->
                    Tag(tag, addTagToList, isSelected = { selectedTags.contains(it) })
                }
            }
        }
    }
}

@Composable
fun Tag(tag: FullTag, addTagToList: (Int) -> Unit, isSelected: (Int) -> Boolean) {
    ElevatedFilterChip(
        selected = isSelected(tag.tagId),
        onClick = { addTagToList(tag.tagId) },
        label = { Text(tag.tagName) },
        trailingIcon = {
            Icon(
                imageVector = tag.tagIcon ?: Icons.Default.Star,
                contentDescription = "${tag.tagName} icon"
            )
        },
        colors = FilterChipDefaults.elevatedFilterChipColors(
            containerColor = tag.containerColor,
            selectedContainerColor = tag.outlineColor
        
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun AddGameScreen_Preview() {
    GameDiaryTheme {
        AddGameScreen(
            viewModel = viewModel(factory = GlobalViewModelProvider.Factory),
            navigateUp = {},
        )
    }
}