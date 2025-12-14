package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.ui.components.ImagePreview
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.DeepRed
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

// Hard-coded list of 4 shared recipes
private val sharedRecipes = listOf(
    Recipe(
        id = "shared_1",
        title = "Grilled Salmon with Asparagus",
        description = "A healthy and delicious weeknight dinner. The salmon is cooked to perfection and served with crisp-tender asparagus.",
        ingredients = listOf("1 lb salmon fillets", "1 bunch asparagus", "2 tbsp olive oil", "1 lemon", "Salt and pepper"),
        tools = listOf("Grill", "Tongs"),
        steps = listOf("Preheat grill to medium-high.", "Toss asparagus with 1 tbsp olive oil, salt, and pepper.", "Grill asparagus for 5-7 minutes, turning occasionally.", "Season salmon with salt, pepper, and remaining olive oil. Grill for 4-6 minutes per side.", "Serve salmon with a squeeze of lemon and grilled asparagus."),
        imageUrls = listOf("https://images.pexels.com/photos/3763847/pexels-photo-3763847.jpeg"),
        totalTimeMinutes = 20,
        rating = 4.8f,
        difficulty = Difficulty.EASY
    ),
    Recipe(
        id = "shared_3",
        title = "Mushroom Risotto",
        description = "A creamy and comforting classic Italian dish. Perfect for a cozy night in.",
        ingredients = listOf("1 tbsp olive oil", "1/2 onion, chopped", "8 oz mushrooms, sliced", "1 1/2 cups Arborio rice", "1/2 cup dry white wine", "4 cups chicken or vegetable broth, warmed", "1/2 cup grated Parmesan cheese"),
        tools = listOf("Large saucepan", "Wooden spoon"),
        steps = listOf("Heat olive oil in a saucepan. Add onion and cook until soft. Add mushrooms and cook until browned.", "Stir in Arborio rice and cook for 1 minute.", "Pour in wine and cook until absorbed. Add broth, one ladleful at a time, stirring until each is absorbed before adding the next.", "Once rice is tender and creamy, stir in Parmesan cheese. Season with salt and pepper."),
        imageUrls = listOf("https://images.pexels.com/photos/5409013/pexels-photo-5409013.jpeg"),
        totalTimeMinutes = 45,
        rating = 4.9f,
        difficulty = Difficulty.HARD
    ),
    Recipe(
        id = "shared_4",
        title = "Caprese Salad",
        description = "A simple and elegant salad that highlights the fresh flavors of summer.",
        ingredients = listOf("2 large ripe tomatoes, sliced", "8 oz fresh mozzarella cheese, sliced", "Fresh basil leaves", "Balsamic glaze", "Extra virgin olive oil", "Salt and pepper"),
        tools = listOf("Knife", "Platter"),
        steps = listOf("Arrange tomato and mozzarella slices on a platter, alternating them.", "Tuck fresh basil leaves in between the slices.", "Drizzle with balsamic glaze and olive oil.", "Season with salt and pepper to taste. Serve immediately."),
        imageUrls = listOf("https://images.pexels.com/photos/1211887/pexels-photo-1211887.jpeg"),
        totalTimeMinutes = 10,
        rating = 4.6f,
        difficulty = Difficulty.EASY
    ),
    Recipe(
        id = "shared_5",
        title = "Chocolate Avocado Mousse",
        description = "A surprisingly healthy and decadent dessert. You won't even taste the avocado!",
        ingredients = listOf("2 ripe avocados", "1/2 cup unsweetened cocoa powder", "1/2 cup maple syrup", "1/4 cup almond milk", "1 tsp vanilla extract", "Pinch of salt"),
        tools = listOf("Food processor or blender"),
        steps = listOf("Combine all ingredients in a food processor or high-speed blender.", "Blend until completely smooth.", "Chill in the refrigerator for at least 30 minutes before serving."),
        imageUrls = listOf("https://images.pexels.com/photos/6061396/pexels-photo-6061396.jpeg"),
        totalTimeMinutes = 10,
        rating = 4.5f,
        difficulty = Difficulty.EASY
    )
)

@Composable
fun BrowseScreen(
    onRecipeClick: (Recipe) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Shared Recipes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = EspressoBrown
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Curated picks from the community",
            style = MaterialTheme.typography.bodyMedium,
            color = DeepRed
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(sharedRecipes) { recipe ->
                GeneratedRecipeCard(recipe, onClick = { onRecipeClick(recipe) })
            }
        }
    }
}

@Composable
private fun GeneratedRecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = EspressoBrown
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            val imageUrl = recipe.imageUrls.firstOrNull()
            if (imageUrl != null) {
                ImagePreview(
                    imageUrl = imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(LightGray)
                ) // Placeholder
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EspressoBrown
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    color = EspressoBrown.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Total time",
                            modifier = Modifier.size(18.dp),
                            tint = AccentOrange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${recipe.totalTimeMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = EspressoBrown.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = recipe.difficulty.name.let { it.first() + it.substring(1).lowercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentOrange,
                        modifier = Modifier
                            .background(AccentOrange.copy(alpha = 0.15f), shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}