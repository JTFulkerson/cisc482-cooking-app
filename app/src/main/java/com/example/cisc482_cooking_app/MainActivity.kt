package com.example.cisc482_cooking_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cisc482_cooking_app.model.Allergy
import com.example.cisc482_cooking_app.model.User
import com.example.cisc482_cooking_app.navigation.Screen
import com.example.cisc482_cooking_app.ui.components.BottomNavigationBar
import com.example.cisc482_cooking_app.ui.screens.BrowseScreen
import com.example.cisc482_cooking_app.ui.screens.ProfileScreen
import com.example.cisc482_cooking_app.ui.screens.RecipesScreen
import com.example.cisc482_cooking_app.ui.screens.ScannerScreen
import com.example.cisc482_cooking_app.data.ai.GeminiRepository
import com.example.cisc482_cooking_app.data.ai.GeminiService
import com.example.cisc482_cooking_app.ui.screens.GenerateRecipeScreen
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import com.example.cisc482_cooking_app.ui.theme.Cream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CISC482CookingAppTheme {
                val geminiRepository = remember {
                    GeminiRepository(GeminiService(BuildConfig.GEMINI_API_KEY))
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Cream
                ) {
                    CollegeFridgeApp(
                        geminiRepository = geminiRepository,
                        ingredientOptions = DefaultIngredientOptions,
                        supplyOptions = DefaultSupplyOptions
                    )
                }
            }
        }
    }
}

private val DefaultIngredientOptions = listOf(
    "All-purpose Flour",
    "Almonds",
    "Apples",
    "Avocado",
    "Bacon",
    "Baking Powder",
    "Baking Soda",
    "Basil",
    "Beans",
    "Beef Broth",
    "Bell Pepper",
    "Blueberries",
    "Bread Crumbs",
    "Broccoli",
    "Brown Rice",
    "Brown Sugar",
    "Butter",
    "Cabbage",
    "Carrots",
    "Cashews",
    "Cauliflower",
    "Celery",
    "Cheddar Cheese",
    "Chicken Breast",
    "Chicken Stock",
    "Chickpeas",
    "Chili Powder",
    "Chocolate Chips",
    "Cilantro",
    "Cinnamon",
    "Coconut Milk",
    "Cornmeal",
    "Cornstarch",
    "Cream Cheese",
    "Cucumber",
    "Cumin",
    "Eggplant",
    "Eggs",
    "Feta Cheese",
    "Fish Sauce",
    "Garlic",
    "Ginger",
    "Granulated Sugar",
    "Green Beans",
    "Green Onion",
    "Ground Beef",
    "Ground Turkey",
    "Honey",
    "Hot Sauce",
    "Italian Seasoning",
    "Kale",
    "Ketchup",
    "Lemons",
    "Lentils",
    "Lettuce",
    "Lime Juice",
    "Maple Syrup",
    "Milk",
    "Mushrooms",
    "Mustard",
    "Nutmeg",
    "Oats",
    "Olive Oil",
    "Onion",
    "Oregano",
    "Paprika",
    "Parmesan",
    "Parsley",
    "Peanut Butter",
    "Peanuts",
    "Pecans",
    "Pesto",
    "Pineapple",
    "Pinto Beans",
    "Pork Chops",
    "Potatoes",
    "Pumpkin Puree",
    "Quinoa",
    "Raisins",
    "Red Cabbage",
    "Red Onion",
    "Rice Vinegar",
    "Rosemary",
    "Salsa",
    "Salmon",
    "Sea Salt",
    "Sesame Oil",
    "Shrimp",
    "Sour Cream",
    "Soy Sauce",
    "Spinach",
    "Sun-dried Tomatoes",
    "Sweet Potatoes",
    "Thyme",
    "Tofu",
    "Tomato Paste",
    "Tomatoes",
    "Tortillas",
    "Vanilla Extract",
    "Yogurt"
)

private val DefaultSupplyOptions = listOf(
    "Baking Sheet",
    "Blender",
    "Bottle Opener",
    "Box Grater",
    "Bread Knife",
    "Can Opener",
    "Cast Iron Skillet",
    "Chef's Knife",
    "Colander",
    "Cooling Rack",
    "Cutting Board",
    "Dutch Oven",
    "Fish Spatula",
    "Food Processor",
    "Garlic Press",
    "Hand Mixer",
    "Instant-read Thermometer",
    "Kitchen Shears",
    "Ladle",
    "Measuring Cups",
    "Measuring Spoons",
    "Microplane Zester",
    "Mixing Bowls",
    "Muffin Pan",
    "Offset Spatula",
    "Oven Mitts",
    "Paring Knife",
    "Peeler",
    "Pizza Cutter",
    "Potholders",
    "Pressure Cooker",
    "Rolling Pin",
    "Salad Spinner",
    "Saucepan",
    "Saute Pan",
    "Sheet Pan",
    "Sieve",
    "Skewers",
    "Slow Cooker",
    "Spatula",
    "Spider Strainer",
    "Stand Mixer",
    "Stockpot",
    "Tongs",
    "Whisk",
    "Wooden Spoon"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeFridgeApp(
    geminiRepository: GeminiRepository,
    ingredientOptions: List<String>,
    supplyOptions: List<String>
) {
    val navController = rememberNavController()

    var userState by remember {
        mutableStateOf(
            User(
                id = "12345",
                name = "John",
                email = "jtfulky@udel.edu",
                hashedPassword = "a_very_secure_placeholder_hash", // Added required password hash
                allergies = listOf(
                    Allergy.SOY, Allergy.EGGS, Allergy.PEANUTS, Allergy.FISH, Allergy.SESAME,
                    Allergy.SHELLFISH, Allergy.TREE_NUTS, Allergy.MILK, Allergy.WHEAT, Allergy.GLUTEN
                ),

                customAllergy = null
            )
        )
    }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "College Fridge",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu icon click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Scanner.route) { ScannerScreen() }
            composable(Screen.Browse.route) { BrowseScreen() }
            composable(Screen.Recipes.route) {
                RecipesScreen(
                    onGenerateRecipe = {
                        navController.navigate(Screen.GenerateRecipe.route)
                    }
                )
            }
            composable(Screen.GenerateRecipe.route) {
                GenerateRecipeScreen(
                    ingredientOptions = ingredientOptions,
                    supplyOptions = supplyOptions,
                    generateRecipe = { request ->
                        geminiRepository.generateRecipeFromSelections(request)
                    }
                )
            }

            // Pass the corrected state and update function to ProfileScreen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    user = userState,
                    onUserChange = { updatedUser ->
                        userState = updatedUser
                    }
                )
            }
        }
    }
}
