package com.example.cisc482_cooking_app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cisc482_cooking_app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Defines all possible states of the scanner screen
private sealed interface ScannerState {
    object Ready : ScannerState // Camera is ready to scan
    object Loading : ScannerState // Waiting for Gemini
    data class Success(val ingredients: List<String>) : ScannerState // Ingredients found
    data class Error(val message: String) : ScannerState // An error occurred
}

@Composable
fun ScannerScreen(pantryViewModel: PantryViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val cameraController = remember { LifecycleCameraController(context) }

    var screenState by remember { mutableStateOf<ScannerState>(ScannerState.Ready) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // region Camera Permission Handling
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCamPermission = granted }
    )
    LaunchedEffect(key1 = true) {
        if (!hasCamPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    // endregion

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    PreviewView(it).apply {
                        controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
        } else {
            // Show a black screen if permission is not granted
            Box(modifier = Modifier.fillMaxSize().background(Color.Black))
        }

        // Show the capture button only when the camera is ready
        if (screenState == ScannerState.Ready) {
            IconButton(
                onClick = {
                    screenState = ScannerState.Loading
                    takePicture(
                        cameraController = cameraController,
                        executor = ContextCompat.getMainExecutor(context),
                        onImageCaptured = { bitmap ->
                            coroutineScope.launch {
                                val result = getIngredientsFromGemini(bitmap)
                                screenState = if (result.isSuccess) {
                                    ScannerState.Success(result.getOrDefault(emptyList()))
                                } else {
                                    ScannerState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                                }
                            }
                        }
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Handle the different states of the screen
        when (val state = screenState) {
            is ScannerState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ScannerState.Success -> {
                IngredientSelectionDialog(
                    ingredients = state.ingredients,
                    onDismiss = { screenState = ScannerState.Ready },
                    onConfirm = {
                        pantryViewModel.addPantryItems(it)
                        showConfirmationDialog = true
                        screenState = ScannerState.Ready // Reset the scanner state
                    }
                )
            }
            is ScannerState.Error -> {
                ErrorDialog(
                    message = state.message,
                    onDismiss = { screenState = ScannerState.Ready }
                )
            }
            is ScannerState.Ready -> {
                // The camera and capture button are visible in this state
            }
        }

        // The final confirmation pop-up
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text("Groceries Added") },
                text = { Text("All selected groceries have been added to your pantry.") },
                confirmButton = {
                    TextButton(onClick = { showConfirmationDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

/**
 * A dialog that shows the list of identified ingredients with checkboxes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngredientSelectionDialog(
    ingredients: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    val selectedIngredients = remember { mutableStateListOf<String>().also { it.addAll(ingredients) } }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Add to Pantry",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(ingredients) { ingredient ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (selectedIngredients.contains(ingredient)) {
                                        selectedIngredients.remove(ingredient)
                                    } else {
                                        selectedIngredients.add(ingredient)
                                    }
                                 }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedIngredients.contains(ingredient),
                                onCheckedChange = null // Click is handled by the Row
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(ingredient)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onConfirm(selectedIngredients.toList()) }) {
                        Text("Add to Pantry")
                    }
                }
            }
        }
    }
}

/**
 * A simple dialog to show an error message.
 */
@Composable
private fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Try Again")
            }
        }
    )
}

/**
 * Takes a picture using the provided [cameraController] and returns a [Bitmap].
 */
private fun takePicture(
    cameraController: LifecycleCameraController,
    executor: java.util.concurrent.Executor,
    onImageCaptured: (Bitmap) -> Unit
) {
    cameraController.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply { postRotate(image.imageInfo.rotationDegrees.toFloat()) }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(), 0, 0, image.width, image.height, matrix, true
                )
                onImageCaptured(rotatedBitmap)
                image.close()
            }
        }
    )
}

/**
 * Calls the Gemini API to identify ingredients from a given [Bitmap].
 * Returns a [Result] containing the list of ingredients or an exception.
 */
private suspend fun getIngredientsFromGemini(image: Bitmap): Result<List<String>> {
    return withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro-vision",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val inputContent = content {
                image(image)
                text("What food ingredients are in this image? Provide a simple, comma-separated list.")
            }
            val response = generativeModel.generateContent(inputContent)
            val ingredients = response.text
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?: emptyList()
            Result.success(ingredients)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
