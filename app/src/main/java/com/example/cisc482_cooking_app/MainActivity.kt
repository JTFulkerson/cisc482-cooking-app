package com.example.cisc482_cooking_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cisc482_cooking_app.ui.screens.GenerateRecipeScreen
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CISC482CookingAppTheme {
                Surface {
                    GenerateRecipeScreen(
                        ingredientOptions = listOf(
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
                        ),
                        supplyOptions = listOf(
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
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CISC482CookingAppTheme {
        Greeting("Android")
    }
}