package com.example.cisc482_cooking_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme

@Composable
fun ImagePreview(
	imageUrl: String,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	contentScale: ContentScale = ContentScale.Crop
) {
	val context = LocalContext.current

	AsyncImage(
		model = ImageRequest.Builder(context)
			.data(imageUrl)
			.crossfade(true)
			.build(),
		contentDescription = contentDescription,
		modifier = modifier,
		contentScale = contentScale
	)
}