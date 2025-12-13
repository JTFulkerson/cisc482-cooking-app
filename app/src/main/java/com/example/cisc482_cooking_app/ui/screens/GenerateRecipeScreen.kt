package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cisc482_cooking_app.data.ai.GeminiRecipeRequest
import com.example.cisc482_cooking_app.data.ai.GeminiResult
import com.example.cisc482_cooking_app.model.Allergy
import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.components.LoadingPopup
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@Composable
fun GenerateRecipeScreen(
    ingredientOptions: List<String> = emptyList(),
    supplyOptions: List<String> = emptyList(),
    generateRecipe: (suspend (GeminiRecipeRequest) -> GeminiResult<String>)? = null,
    onRecipeGenerated: ((Recipe) -> Unit)? = null
) {
    var ingredientQuery by rememberSaveable { mutableStateOf("") }
    val filteredIngredients = ingredientOptions.filter { option ->
        ingredientQuery.isBlank() || option.contains(ingredientQuery, ignoreCase = true)
    }
    val suggestionLimit = 5
    val suggestions = filteredIngredients.take(suggestionLimit)
    val showSuggestions = ingredientQuery.isNotBlank() && suggestions.isNotEmpty()
    var supplyQuery by rememberSaveable { mutableStateOf("") }
    val filteredSupplies = supplyOptions.filter { option ->
        supplyQuery.isBlank() || option.contains(supplyQuery, ignoreCase = true)
    }
    val supplySuggestions = filteredSupplies.take(suggestionLimit)
    val showSupplySuggestions = supplyQuery.isNotBlank() && supplySuggestions.isNotEmpty()
    var selectedIngredients by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var selectedSupplies by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var selectedAllergyNames by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var customAllergyText by rememberSaveable { mutableStateOf("") }
    var additionalRequest by rememberSaveable { mutableStateOf("") }
    var isGenerating by rememberSaveable { mutableStateOf(false) }
    var generatedRecipePreview by rememberSaveable { mutableStateOf<String?>(null) }
    var generationError by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val allergies = Allergy.values().toList()
    val columnSize = (allergies.size + 3 - 1) / 3
    val allergyColumns = allergies.chunked(columnSize)
    val otherSelected = Allergy.OTHER.name in selectedAllergyNames

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Have AI Chef create you a recipe",
            style = MaterialTheme.typography.headlineLarge,
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium,
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                OutlinedTextField(
                    value = ingredientQuery,
                    onValueChange = { ingredientQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Search ingredients") },
                    singleLine = true
                )

                if (showSuggestions) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            suggestions.forEach { ingredient ->
                                Text(
                                    text = ingredient,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (ingredient !in selectedIngredients) {
                                                selectedIngredients = selectedIngredients + ingredient
                                            }
                                            ingredientQuery = ""
                                        }
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        when {
            ingredientOptions.isEmpty() -> {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No ingredients available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ingredientQuery.isNotBlank() && suggestions.isEmpty() -> {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "No matches found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedIngredients.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Selected ingredients",
                    style = MaterialTheme.typography.titleSmall,
                    color = AccentOrange
                )
                Spacer(modifier = Modifier.height(6.dp))
                selectedIngredients.forEach { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = ingredient,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = {
                                selectedIngredients = selectedIngredients.filterNot { it == ingredient }
                            }
                        ) {
                            Text(text = "Remove")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Supplies",
            style = MaterialTheme.typography.titleMedium,
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                OutlinedTextField(
                    value = supplyQuery,
                    onValueChange = { supplyQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Search supplies") },
                    singleLine = true
                )

                if (showSupplySuggestions) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            supplySuggestions.forEach { supply ->
                                Text(
                                    text = supply,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (supply !in selectedSupplies) {
                                                selectedSupplies = selectedSupplies + supply
                                            }
                                            supplyQuery = ""
                                        }
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        when {
            supplyOptions.isEmpty() -> {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No supplies available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            supplyQuery.isNotBlank() && supplySuggestions.isEmpty() -> {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "No matches found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedSupplies.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Selected supplies",
                    style = MaterialTheme.typography.titleSmall,
                    color = AccentOrange
                )
                Spacer(modifier = Modifier.height(6.dp))
                selectedSupplies.forEach { supply ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = supply,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = {
                                selectedSupplies = selectedSupplies.filterNot { it == supply }
                            }
                        ) {
                            Text(text = "Remove")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Any allergies or preferences?",
            style = MaterialTheme.typography.titleMedium,
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            allergyColumns.forEach { column ->
                AllergyColumn(
                    modifier = Modifier.weight(1f),
                    allergies = column,
                    selectedAllergyNames = selectedAllergyNames,
                    onToggle = { name ->
                        selectedAllergyNames = selectedAllergyNames.toggleSelection(name)
                    }
                )
            }
        }

        if (otherSelected) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = customAllergyText,
                onValueChange = { customAllergyText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Describe other allergy") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = additionalRequest,
            onValueChange = { additionalRequest = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Any special requests?") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val generator = generateRecipe ?: return@Button
                val allergyList = selectedAllergyNames.mapNotNull { name ->
                    runCatching { Allergy.valueOf(name).displayName }.getOrNull()
                }.toMutableList()
                if (otherSelected && customAllergyText.isNotBlank()) {
                    allergyList.add(customAllergyText.trim())
                }
                val request = GeminiRecipeRequest(
                    ingredients = selectedIngredients,
                    supplies = selectedSupplies,
                    allergies = allergyList,
                    customRequest = additionalRequest.takeIf { it.isNotBlank() }
                )

                coroutineScope.launch {
                    isGenerating = true
                    generationError = null
                    generatedRecipePreview = null
                    when (val result = generator.invoke(request)) {
                        is GeminiResult.Success -> {
                            val parsedRecipe = parseRecipeJson(result.data)
                            if (parsedRecipe != null) {
                                if (onRecipeGenerated != null) {
                                    onRecipeGenerated(parsedRecipe)
                                } else {
                                    generatedRecipePreview = parsedRecipe.title
                                }
                            } else {
                                generationError = "Unable to parse recipe output."
                                generatedRecipePreview = result.data
                            }
                        }
                        is GeminiResult.Error -> generationError = result.message
                    }
                    isGenerating = false
                }
            },
            enabled = generateRecipe != null && !isGenerating,
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentOrange,
                contentColor = androidx.compose.ui.graphics.Color.White
            )
        ) {
            Text(text = if (isGenerating) "Creating..." else "Create")
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isGenerating -> LoadingPopup()
            generationError != null -> {
                Text(
                    text = generationError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            generatedRecipePreview != null -> {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = generatedRecipePreview.orEmpty(),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun List<String>.toggleSelection(item: String): List<String> =
    if (contains(item)) filterNot { it == item } else this + item

@Composable
private fun AllergyColumn(
    modifier: Modifier = Modifier,
    allergies: List<Allergy>,
    selectedAllergyNames: List<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = modifier) {
        allergies.forEach { allergy ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = allergy.name in selectedAllergyNames,
                    onCheckedChange = { onToggle(allergy.name) }
                )
                Text(
                    text = allergy.displayName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private val Allergy.displayName: String
    get() = name
        .lowercase()
        .replace('_', ' ')
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

private fun parseRecipeJson(raw: String): Recipe? = runCatching {
    val json = JSONObject(raw)
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

@Preview(showBackground = true)
@Composable
fun GenerateRecipeScreenPreview() {
    CISC482CookingAppTheme {
        Surface {
            GenerateRecipeScreen(
                ingredientOptions = listOf(
                    "Chicken",
                    "Basil",
                    "Tomato",
                    "Garlic",
                    "Olive Oil"
                ),
                supplyOptions = listOf(
                    "Mixing Bowl",
                    "Spatula",
                    "Whisk",
                    "Saucepan",
                    "Cutting Board"
                ),
                generateRecipe = {
                    GeminiResult.Success(
                        "{" +
                            "\"id\":\"demo\"," +
                            "\"title\":\"Demo Recipe\"," +
                            "\"description\":\"Tasty dish\"," +
                            "\"ingredients\":[\"Item 1\",\"Item 2\",\"Item 3\"]," +
                            "\"tools\":[\"Pan\"]," +
                            "\"steps\":[\"Do thing\",\"Serve\"]," +
                            "\"imageUrls\":[]," +
                            "\"totalTimeMinutes\":20," +
                            "\"rating\":4.5," +
                            "\"difficulty\":\"EASY\"" +
                        "}"
                    )
                }
            )
        }
    }
}
