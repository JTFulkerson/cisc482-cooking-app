package com.example.cisc482_cooking_app.ui.screens

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
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RecipeSuggestion(val title: String, val description: String)

private sealed interface BrowseState {
    object Loading : BrowseState
    data class Success(val recipes: List<RecipeSuggestion>) : BrowseState
    data class Error(val message: String) : BrowseState
    object EmptyPantry : BrowseState // New state for when the pantry is empty
}

@Composable
fun BrowseScreen(pantryViewModel: PantryViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf<BrowseState>(BrowseState.Loading) }
    val pantryIngredients = pantryViewModel.pantryItems

    // Automatically trigger recipe generation when the screen is viewed
    // or when the pantry ingredients change.
    LaunchedEffect(key1 = pantryIngredients.toList()) { // Use toList() to trigger on content change
        if (pantryIngredients.isNotEmpty()) {
            state = BrowseState.Loading
            coroutineScope.launch {
                val result = generateRecipeSuggestions(pantryIngredients)
                state = if (result.isSuccess) {
                    BrowseState.Success(result.getOrDefault(emptyList()))
                } else {
                    BrowseState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        } else {
            state = BrowseState.EmptyPantry
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recipe Browser",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Found ${pantryIngredients.size} ingredients in your pantry.")

        Spacer(modifier = Modifier.height(24.dp))

        when (val currentState = state) {
            is BrowseState.Loading -> {
                CircularProgressIndicator()
            }
            is BrowseState.Success -> {
                if (currentState.recipes.isEmpty()) {
                    Text("No recipe suggestions found for your current ingredients.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(currentState.recipes) { recipe ->
                            RecipeSuggestionCard(recipe)
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
                    text = "Add ingredients to your pantry via the Scanner or Pantry tabs to get recipe suggestions.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RecipeSuggestionCard(recipe: RecipeSuggestion) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = recipe.description)
        }
    }
}

private suspend fun generateRecipeSuggestions(ingredients: List<String>): Result<List<RecipeSuggestion>> {
    if (ingredients.isEmpty()) {
        return Result.success(emptyList())
    }

    return withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash-latest",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val prompt = "Based on the following ingredients, suggest some recipes. Format the response as a simple JSON array of objects, where each object has a 'title' and a 'description'. Ingredients: ${ingredients.joinToString(", ")}"
            val inputContent = content { text(prompt) }

            val response = generativeModel.generateContent(inputContent)
            val suggestions = parseRecipeSuggestions(response.text ?: "")
            Result.success(suggestions)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

private fun parseRecipeSuggestions(jsonString: String): List<RecipeSuggestion> {
    // A simple, non-robust JSON parser. A real app should use a library like Gson or kotlinx.serialization
    return try {
        val cleanJson = jsonString.substringAfter("```json").substringBefore("```").trim()
        val jsonArray = org.json.JSONArray(cleanJson)
        (0 until jsonArray.length()).map {
            val jsonObject = jsonArray.getJSONObject(it)
            RecipeSuggestion(
                title = jsonObject.getString("title"),
                description = jsonObject.getString("description")
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
