package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.ui.components.BottomNavigationBar
import com.example.cisc482_cooking_app.ui.components.RecipeCard
import com.example.cisc482_cooking_app.ui.components.RecipeData
import com.example.cisc482_cooking_app.ui.components.omeletteData
import com.example.cisc482_cooking_app.ui.components.pBJData
import com.example.cisc482_cooking_app.ui.components.tacoData
import kotlin.collections.emptyList
import kotlin.collections.mutableListOf

@Composable
fun RecipeScreen(savedRecipes: List<RecipeData>, onGenerateRecipe: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Recipes:", modifier = Modifier
            .padding(8.dp),
            fontSize = 40.sp)
        Spacer(modifier = Modifier.height(16.dp))

        savedRecipes.forEach { recipe ->
            RecipeCard(recipe) }

        Text(text = "Want something new? Generate a new recipe!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGenerateRecipe) {
            Text(text = "Generate Recipe")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeScreen(){
    RecipeScreen(listOf(tacoData, omeletteData, pBJData),{})
}