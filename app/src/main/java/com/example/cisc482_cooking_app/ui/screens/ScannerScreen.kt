package com.example.cisc482_cooking_app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cisc482_cooking_app.BuildConfig
import com.example.cisc482_cooking_app.model.PantryViewModel
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.DeepRed
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
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
            PermissionOverlay(
                modifier = Modifier.fillMaxSize(),
                onRequestAgain = { permissionLauncher.launch(Manifest.permission.CAMERA) }
            )
        }

        if (screenState == ScannerState.Ready) {
            FilledIconButton(
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
                    .size(88.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AccentOrange,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Take picture",
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        if (hasCamPermission) {
            Text(
                text = "Align your groceries and tap capture",
                color = Cream,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
                    .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
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
                    CircularProgressIndicator(color = AccentOrange)
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
                containerColor = Cream,
                title = { Text("Groceries Added", color = EspressoBrown, fontWeight = FontWeight.Bold) },
                text = { Text("All selected groceries have been added to your pantry.", color = EspressoBrown.copy(alpha = 0.85f)) },
                confirmButton = {
                    TextButton(
                        onClick = { showConfirmationDialog = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = DeepRed)
                    ) {
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
                .clip(RoundedCornerShape(28.dp)),
            color = Cream,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Add to Pantry",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = EspressoBrown
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (ingredients.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGray, RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No ingredients recognized.",
                            color = DeepRed,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 320.dp)
                    ) {
                        items(ingredients) { ingredient ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .clickable {
                                        if (selectedIngredients.contains(ingredient)) {
                                            selectedIngredients.remove(ingredient)
                                        } else {
                                            selectedIngredients.add(ingredient)
                                        }
                                    }
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                            ) {
                                Checkbox(
                                    checked = selectedIngredients.contains(ingredient),
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = AccentOrange,
                                        uncheckedColor = EspressoBrown.copy(alpha = 0.6f),
                                        checkmarkColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    ingredient,
                                    color = EspressoBrown,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = DeepRed)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(selectedIngredients.toList()) },
                        enabled = selectedIngredients.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentOrange,
                            contentColor = Color.White
                        )
                    ) {
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
        containerColor = Cream,
        title = { Text("Error", color = DeepRed, fontWeight = FontWeight.Bold) },
        text = { Text(message, color = EspressoBrown) },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = DeepRed)
            ) {
                Text("Try Again")
            }
        }
    )
}

@Composable
private fun PermissionOverlay(
    modifier: Modifier = Modifier,
    onRequestAgain: () -> Unit
) {
    Surface(modifier = modifier, color = Cream) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Camera access is required to scan groceries",
                style = MaterialTheme.typography.headlineSmall,
                color = EspressoBrown,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Please grant permission to continue",
                color = DeepRed,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRequestAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = Color.White
                )
            ) {
                Text("Grant Permission")
            }
        }
    }
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
                modelName = "gemini-2.5-flash",
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
