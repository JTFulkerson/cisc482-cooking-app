package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.Transparent
import java.util.UUID

@Composable
fun ManualRecipeScreen(
    onBackClick: () -> Unit,
    onSaveRecipe: (Recipe) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var ingredientsInput by rememberSaveable { mutableStateOf("") }
    var toolsInput by rememberSaveable { mutableStateOf("") }
    var stepsInput by rememberSaveable { mutableStateOf("") }
    var totalTimeInput by rememberSaveable { mutableStateOf("") }
    var imageUrlInput by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf(4f) }
    var selectedDifficulty by rememberSaveable { mutableStateOf(Difficulty.MEDIUM) }

    val ingredientList = ingredientsInput.lines().map { it.trim() }.filter { it.isNotBlank() }
    val toolList = toolsInput.lines().map { it.trim() }.filter { it.isNotBlank() }
    val stepList = stepsInput.lines().map { it.trim() }.filter { it.isNotBlank() }
    val totalTimeMinutes = totalTimeInput.toIntOrNull() ?: 0
    val timeInvalid = totalTimeInput.isNotBlank() && totalTimeMinutes <= 0

    val canSave = title.isNotBlank() && description.isNotBlank() &&
        ingredientList.isNotEmpty() && stepList.isNotEmpty() && totalTimeMinutes > 0

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AccentOrange,
        unfocusedBorderColor = EspressoBrown.copy(alpha = 0.3f),
        cursorColor = AccentOrange,
        focusedLabelColor = AccentOrange,
        unfocusedLabelColor = EspressoBrown.copy(alpha = 0.7f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = EspressoBrown)
            }
            Text(
                text = "Add Recipe",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 2.dp,
            border = BorderStroke(1.dp, EspressoBrown.copy(alpha = 0.1f)),
            color = Cream
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Recipe Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentOrange
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description") },
                    minLines = 3,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = ingredientsInput,
                    onValueChange = { ingredientsInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ingredients (one per line)") },
                    minLines = 4,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = toolsInput,
                    onValueChange = { toolsInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tools (optional, one per line)") },
                    minLines = 3,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = stepsInput,
                    onValueChange = { stepsInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Steps (one per line)") },
                    minLines = 4,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = totalTimeInput,
                    onValueChange = { totalTimeInput = it.filter { char -> char.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Total Time (minutes)") },
                    isError = timeInvalid,
                    supportingText = {
                        if (timeInvalid) {
                            Text(text = "Enter time greater than 0")
                        }
                    },
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = imageUrlInput,
                    onValueChange = { imageUrlInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Image URL (optional)") },
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Difficulty", style = MaterialTheme.typography.titleMedium, color = EspressoBrown)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Difficulty.entries.forEach { option ->
                        FilterChip(
                            selected = option == selectedDifficulty,
                            onClick = { selectedDifficulty = option },
                            label = { Text(option.name.lowercase().replaceFirstChar { it.titlecase() }) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentOrange.copy(alpha = 0.2f),
                                selectedLabelColor = EspressoBrown,
                                containerColor = Transparent,
                                labelColor = EspressoBrown
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Rating: ${"%.1f".format(rating)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = EspressoBrown
                )
                Slider(
                    value = rating,
                    onValueChange = { rating = it.coerceIn(0f, 5f) },
                    valueRange = 0f..5f,
                    colors = SliderDefaults.colors(thumbColor = AccentOrange, activeTrackColor = AccentOrange)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val recipe = Recipe(
                            id = UUID.randomUUID().toString(),
                            title = title.trim(),
                            description = description.trim(),
                            ingredients = ingredientList,
                            tools = toolList,
                            steps = stepList,
                            imageUrls = imageUrlInput.trim().takeIf { it.isNotBlank() }?.let { listOf(it) } ?: emptyList(),
                            totalTimeMinutes = totalTimeMinutes,
                            rating = rating,
                            difficulty = selectedDifficulty
                        )
                        onSaveRecipe(recipe)
                    },
                    enabled = canSave,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentOrange,
                        contentColor = Cream
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Save Recipe", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
