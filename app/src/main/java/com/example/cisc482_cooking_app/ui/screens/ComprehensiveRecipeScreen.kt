package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.components.ImagePreview
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.GardenGreen
import com.example.cisc482_cooking_app.ui.theme.LightGray


@Composable
fun ComprehensiveRecipeScreen(
	recipe: Recipe,
	pantryIngredients: List<String>,
    onBackClick: () -> Unit,
    onSaveRecipe: ((Recipe) -> Unit)? = null
) {
	val heroImageUrl = recipe.imageUrls.firstOrNull()
	val ingredientsOwned = recipe.ingredients.count { it in pantryIngredients }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
            .background(Cream),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EspressoBrown)
            }
        }
        // Title and Image
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (heroImageUrl != null) {
                ImagePreview(
                    imageUrl = heroImageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    color = LightGray
                ) {}
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = EspressoBrown
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time and Ingredient Count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time to cook",
                    modifier = Modifier.size(48.dp),
                    tint = AccentOrange
                )
                Text(text = " ${recipe.totalTimeMinutes} min", fontSize = 18.sp, color = EspressoBrown)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, GardenGreen),
                    color = GardenGreen.copy(alpha = 0.25f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "$ingredientsOwned/${recipe.ingredients.size}", fontSize = 18.sp, color = EspressoBrown)
                    }

                }
                Text(text = "Ingredients", fontSize = 18.sp, color = EspressoBrown)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LightGray, thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients and Tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Ingredients", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = EspressoBrown)
                Spacer(modifier = Modifier.height(8.dp))
                recipe.ingredients.forEach { ingredient ->
                    Text(text = "- $ingredient", color = EspressoBrown)
                }
            }
            VerticalDivider(color = LightGray, thickness = 2.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Tools", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = EspressoBrown)
                Spacer(modifier = Modifier.height(8.dp))
                recipe.tools.forEach { tool ->
                    Text(text = "- $tool", color = EspressoBrown)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LightGray, thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))


        // Steps
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = "Steps", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = EspressoBrown)
            Spacer(modifier = Modifier.height(8.dp))
            recipe.steps.forEachIndexed { index, step ->
                Text(text = "${index + 1}. $step", color = EspressoBrown)
            }

			if (onSaveRecipe != null) {
				Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onSaveRecipe(recipe) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentOrange,
                        contentColor = Cream
                    )
                ) {
					Text(text = "Save Recipe")
				}
			}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComprehensiveRecipeScreen() {
    val pantry = listOf("Bread", "Jelly")
    val previewRecipe = Recipe(
        id = "preview",
        title = "Preview PB&J",
        description = "Classic comfort food",
        ingredients = listOf("Bread", "Peanut Butter", "Jelly"),
        tools = listOf("Knife", "Plate"),
        steps = listOf(
            "Toast bread lightly.",
            "Spread peanut butter.",
            "Add jelly and serve."
        ),
        imageUrls = listOf("https://static01.nyt.com/images/2024/09/27/multimedia/AS-Griddled-PBJ-qljg/AS-Griddled-PBJ-qljg-googleFourByThree.jpg"),
        totalTimeMinutes = 5,
        rating = 4.5f,
        difficulty = Difficulty.EASY
    )
    ComprehensiveRecipeScreen(previewRecipe, pantry, onBackClick = {})
}
