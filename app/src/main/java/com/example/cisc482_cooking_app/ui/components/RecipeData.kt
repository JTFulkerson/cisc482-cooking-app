package com.example.cisc482_cooking_app.ui.components

class RecipeData(
    val name: String,
    val time: Int,
    val ingredients: List<String>,
    val description: String,
    val tools: List<String>,
    val steps: List<String>,
    val img: String
) {

}

val pBJData = RecipeData(
    name = "Peanut Butter & Jelly",
    time = 5,
    ingredients = listOf("Peanut Butter", "Jelly", "Bread"),
    description = "A classic school lunch",
    tools = listOf("Knife", "Toaster"),
    steps = listOf(
        "Take two slices of bread.",
        "Spread peanut butter on one slice and jelly on the other.",
        "Combine the two slices to make a sandwich."
    ),
    img = "pbj"
)

val tacoData = RecipeData(
    name = "Tacos",
    time = 20,
    ingredients = listOf("Taco shells", "Ground beef", "Lettuce", "Tomato", "Cheese"),
    description = "A classic Mexican dish",
    tools = listOf("Frying pan", "Spatula"),
    steps = listOf(
        "Cook ground beef in a frying pan.",
        "Warm up taco shells in the oven.",
        "Fill taco shells with ground beef, lettuce, tomato, and cheese."
    ),
    img = "tacos"
)

val omeletteData = RecipeData(
    name = "Omelette",
    time = 10,
    ingredients = listOf("Eggs", "Cheese", "Milk"),
    description = "A breakfast classic",
    tools = listOf("Frying pan", "Spatula", "Whisk"),
    steps = listOf(
        "Whisk eggs and milk together.",
        "Pour egg mixture into a hot frying pan.",
        "Add cheese and fold the omelette in half."
    ),
    img = "omelette"
)
