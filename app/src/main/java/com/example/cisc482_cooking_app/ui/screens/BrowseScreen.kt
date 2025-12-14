package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cisc482_cooking_app.BuildConfig
import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.components.ImagePreview
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private sealed interface BrowseState {
    object Loading : BrowseState
    data class Success(val recipes: List<Recipe>) : BrowseState
    data class Error(val message: String) : BrowseState
    object EmptyPantry : BrowseState
}

@Composable
fun BrowseScreen(
    onRecipeClick: (Recipe) -> Unit,
    pantryViewModel: PantryViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf<BrowseState>(BrowseState.Loading) }
    val pantryIngredients = pantryViewModel.pantryItems

    LaunchedEffect(key1 = pantryIngredients.toList()) {
        state = if (pantryIngredients.isNotEmpty()) {
            BrowseState.Loading
        } else {
            BrowseState.EmptyPantry
        }

        if (pantryIngredients.isNotEmpty()) {
            coroutineScope.launch {
                val result = generateRecipesFromIngredients(pantryIngredients)
                state = if (result.isSuccess) {
                    BrowseState.Success(result.getOrDefault(emptyList()))
                } else {
                    BrowseState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred.")
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recipe Suggestions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Based on ${pantryIngredients.size} ingredients in your pantry.")

        Spacer(modifier = Modifier.height(24.dp))

        when (val currentState = state) {
            is BrowseState.Loading -> CircularProgressIndicator()
            is BrowseState.Success -> {
                if (currentState.recipes.isEmpty()) {
                    Text("Couldn\'t generate any recipes. Try adding more ingredients to your pantry!")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(currentState.recipes) { recipe ->
                            GeneratedRecipeCard(recipe, onClick = { onRecipeClick(recipe) })
                        }
                    }
                }
            }
            is BrowseState.Error -> {
                Text(
                    text = "Error: ${currentState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is BrowseState.EmptyPantry -> {
                Text(
                    text = "Add ingredients to your pantry via the Pantry tab to get recipe suggestions.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun GeneratedRecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageUrl = recipe.imageUrls.firstOrNull()
            if (imageUrl != null) {
                ImagePreview(
                    imageUrl = imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Box(modifier = Modifier.size(100.dp).background(MaterialTheme.colorScheme.surfaceVariant)) // Placeholder
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recipe.description, maxLines = 3)
            }
        }
    }
}

private suspend fun generateRecipesFromIngredients(ingredients: List<String>): Result<List<Recipe>> {
    return withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash-latest",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val prompt = """
            Based on the following ingredients, generate 3 complete recipes. The recipes should be different from each other.
            Format the response as a single, minified JSON array of recipe objects. Do not include any extra text, markdown, or any other characters outside of the JSON array.
            Each recipe object must have the following fields: 'id' (a new unique UUID), 'title' (string), 'description' (string), 'ingredients' (JSON array of strings), 'tools' (JSON array of strings), 'steps' (JSON array of strings), 'imageUrls' (JSON array of strings), 'totalTimeMinutes' (integer), 'rating' (float between 0.0 and 5.0), and 'difficulty' (one of "EASY", "MEDIUM", or "HARD").

            Ingredients: ${ingredients.joinToString(", ")}
            """

            val response = generativeModel.generateContent(prompt)
            val recipes = parseGeneratedRecipes(response.text ?: "")
            Result.success(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

private fun parseGeneratedRecipes(jsonString: String): List<Recipe> {
    return try {
        val jsonArray = JSONArray(jsonString)
        (0 until jsonArray.length()).mapNotNull { i ->
            val jsonObject = jsonArray.getJSONObject(i)
            parseSingleRecipeJson(jsonObject)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

private fun parseSingleRecipeJson(json: JSONObject): Recipe? = runCatching {
    val id = json.optString("id").ifBlank { UUID.randomUUID().toString() }
    val title = json.getString("title")
    val description = json.getString("description")
    val ingredients = json.getJSONArray("ingredients").toStringList()
    val tools = json.optJSONArray("tools")?.toStringList() ?: emptyList()
    val steps = json.getJSONArray("steps").toStringList()
    val images = json.optJSONArray("imageUrls")?.toStringList() ?: emptyList()
    val totalTime = json.getInt("totalTimeMinutes")
    val rating = json.optDouble("rating", 4.0).toFloat()
    val difficulty = Difficulty.valueOf(json.getString("difficulty").uppercase())

    Recipe(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        tools = tools,
        steps = steps,
        imageUrls = images,
        totalTimeMinutes = totalTime,
        rating = rating,
        difficulty = difficulty
    )
}.getOrNull()

private fun JSONArray.toStringList(): List<String> = buildList(length()) {
    for (index in 0 until length()) {
        add(getString(index))
    }
}
