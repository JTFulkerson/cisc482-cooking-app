package com.example.cisc482_cooking_app.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cisc482_cooking_app.model.PantryViewModel
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.DeepRed
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.SoftWhite

@Composable
fun PantryScreen(
    pantryViewModel: PantryViewModel = viewModel(),
    ingredientOptions: List<String> = emptyList()
) {
    val pantryItems = pantryViewModel.pantryItems
    var newItemText by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    var lastSize by remember { mutableStateOf(pantryItems.size) }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AccentOrange,
        unfocusedBorderColor = EspressoBrown.copy(alpha = 0.3f),
        cursorColor = AccentOrange,
        focusedLabelColor = AccentOrange,
        unfocusedLabelColor = EspressoBrown.copy(alpha = 0.7f)
    )

    LaunchedEffect(pantryItems.size) {
        if (pantryItems.size > lastSize && pantryItems.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
        lastSize = pantryItems.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp)
    ) {
        Text(
            text = "My Pantry",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = EspressoBrown
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Keep track of your staples and recent hauls",
            style = MaterialTheme.typography.bodyMedium,
            color = DeepRed
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Input for adding new items
        val filteredSuggestions = ingredientOptions.filter { option ->
            newItemText.isNotBlank() && option.contains(newItemText, ignoreCase = true)
        }.take(5)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SoftWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemText,
                        onValueChange = { newItemText = it },
                        label = { Text("Add new ingredient") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            pantryViewModel.addPantryItem(newItemText.trim())
                            newItemText = ""
                        },
                        enabled = newItemText.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentOrange,
                            contentColor = Cream
                        )
                    ) {
                        Text("Add")
                    }
                }

                if (filteredSuggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 2.dp,
                        color = SoftWhite,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            filteredSuggestions.forEach { suggestion ->
                                Text(
                                    text = suggestion,
                                    color = EspressoBrown,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            newItemText = suggestion
                                        }
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // List of current pantry items
        if (pantryItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoftWhite, RoundedCornerShape(24.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Your pantry is empty. Add items above!",
                    color = DeepRed,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(pantryItems, key = { it }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = EspressoBrown,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { pantryViewModel.removePantryItem(item) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove $item",
                                    tint = DeepRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
