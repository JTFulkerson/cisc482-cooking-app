package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.components.RecipeCard
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

@Composable
fun RecipeScreen(
    savedRecipes: List<Recipe>,
    onGenerateRecipe: () -> Unit,
    onAddRecipe: () -> Unit,
    onStartClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recipes:",
            modifier = Modifier
                .padding(8.dp),
            fontSize = 40.sp,
            color = EspressoBrown
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 3.dp, color = LightGray)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Want something new? Generate a new recipe!", color = EspressoBrown)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onGenerateRecipe,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = Cream
                )
            ) {
                Text(text = "Generate Recipe")
            }
            OutlinedIconButton(
                onClick = onAddRecipe,
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = EspressoBrown
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add recipe manually"
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Saved Recipes:",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(8.dp),
            color = EspressoBrown
        )
        savedRecipes.forEach { recipe ->
            RecipeCard(recipe, onStartClick) }


    }
}