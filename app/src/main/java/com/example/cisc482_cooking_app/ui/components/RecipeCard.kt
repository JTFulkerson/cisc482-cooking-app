package com.example.cisc482_cooking_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecipeCard(recipe: RecipeData){
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()){
        Row() {
            Column {
                Text(recipe.name)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Green),
                        contentAlignment = Alignment.Center) {
                            Text(recipe.ingredients.size.toString())
                    }
                    Text("Ingredients")
                }
                Icon(imageVector = ){}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeCard(){
    RecipeCard(pBJData)
}