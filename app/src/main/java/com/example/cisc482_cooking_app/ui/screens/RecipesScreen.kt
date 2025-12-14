package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown

@Composable
fun RecipesScreen(onGenerateRecipe: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Browse your saved recipes or create something new.",
                color = EspressoBrown
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onGenerateRecipe,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = Cream
                )
            ) {
                Text(text = "Generate Recipe")
            }
        }
    }
}