package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cisc482_cooking_app.ui.components.RecipeData
import kotlin.collections.emptyList
import kotlin.collections.mutableListOf

@Composable
fun RecipeScreen() {
    val savedRecipes = remember { mutableStateListOf<RecipeData>() }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)){
        Row {
            Column {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeScreen(){
    RecipeScreen()
}