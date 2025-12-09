package com.example.cisc482_cooking_app.ui.screens

import android.Manifest
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.cisc482_cooking_app.BuildConfig
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ScannerScreenState {
    object Ready : ScannerScreenState
    object Loading : ScannerScreenState
    data class Success(val ingredients: String) : ScannerScreenState
    data class Error(val message: String) : ScannerScreenState
}

@Composable
fun ScannerScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        if (!hasCamPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    var screenState: ScannerScreenState by remember { mutableStateOf(ScannerScreenState.Ready) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
            CameraView(controller, lifecycleOwner, Modifier.fillMaxSize())
        } else {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                screenState = ScannerScreenState.Loading
                takePicture(controller, ContextCompat.getMainExecutor(context)) { bitmap ->
                    coroutineScope.launch {
                        val result = getIngredientsFromGemini(bitmap)
                        screenState = if (result.isNotBlank()) {
                            ScannerScreenState.Success(result)
                        } else {
                            ScannerScreenState.Error("Could not identify ingredients.")
                        }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Take picture",
                    tint = AccentOrange
                )
            }
        }

        when (val state = screenState) {
            is ScannerScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentOrange)
                }
            }
            is ScannerScreenState.Success -> {
                ResultView(message = state.ingredients) {
                    screenState = ScannerScreenState.Ready
                }
            }
            is ScannerScreenState.Error -> {
                 ResultView(message = state.message) {
                    screenState = ScannerScreenState.Ready
                }
            }
            is ScannerScreenState.Ready -> {}
        }
    }
}

@Composable
private fun CameraView(
    controller: LifecycleCameraController,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(factory = {
        PreviewView(it).apply {
            this.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    }, modifier = modifier)
}

private fun takePicture(
    controller: LifecycleCameraController,
    executor: java.util.concurrent.Executor,
    onImageCaptured: (Bitmap) -> Unit
) {
    controller.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(), 0, 0, image.width, image.height, matrix, true
                )
                onImageCaptured(rotatedBitmap)
                image.close()
            }
        }
    )
}

@Composable
private fun ResultView(message: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Button(
                onClick = onDismiss, 
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
            ) {
                Text("Scan Again")
            }
        }
    }
}

private suspend fun getIngredientsFromGemini(image: Bitmap): String {
    return withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro-vision",
                apiKey = BuildConfig.GEMINI_API_KEY
            )

            val inputContent = content {
                image(image)
                text("What ingredients are in this image? Give me a comma separated list.")
            }

            val response = generativeModel.generateContent(inputContent)
            response.text ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.localizedMessage}"
        }
    }
}
