package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

data class RecipeDetails(
    val id: String,
    val title: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: String
)

val sampleRecipeDetails = RecipeDetails(
    id = "1",
    title = "Classic Tomato Bruschetta",
    imageUrl = "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
    ingredients = listOf(
        "4 medium tomatoes, diced",
        "1/4 cup fresh basil, chopped",
        "2 cloves garlic, minced",
        "1 tbsp balsamic vinegar",
        "1 tbsp olive oil",
        "Salt and pepper to taste",
        "1 baguette, sliced"
    ),
    instructions = "1. In a bowl, combine tomatoes, basil, garlic, balsamic vinegar, and olive oil. Season with salt and pepper.\n2. Toast the baguette slices until golden brown.\n3. Top each slice with the tomato mixture and serve immediately."
)

@Composable
fun RecipeDetailScreen(recipe: RecipeDetails, onImport: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = recipe.imageUrl).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            ),
            contentDescription = recipe.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recipe.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Ingredients", fontWeight = FontWeight.Bold)
            recipe.ingredients.forEach { ingredient ->
                Text(text = "• $ingredient")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Instructions", fontWeight = FontWeight.Bold)
            Text(text = recipe.instructions)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onImport(recipe.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Import Recipe")
            }
        }
    }
}
