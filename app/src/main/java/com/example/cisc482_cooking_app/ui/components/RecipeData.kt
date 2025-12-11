package com.example.cisc482_cooking_app.ui.components

class RecipeData(
    val name: String,
    val time: Int,
    val ingredients: List<String>,
    val tools: List<String>,
    val description: String,
    val img: String?
) {

}

val pBJData = RecipeData(
    name = "Peanut Butter & Jelly",
    time = 5,
    ingredients = listOf("Peanut Butter", "Jelly", "Bread"),
    tools = listOf("Toaster", "Knife"),
    description = "A classic school lunch",
    img = "pbj"
)

val tacoData = RecipeData(
    name = "Tacos",
    time = 30,
    ingredients = listOf("Tortillas", "Ground Beef", "Lettuce", "Salsa", "Sour Cream"),
    tools = listOf("Plate", "Oven"),
    description = "A popular Mexican dish",
    img = "tacos"
)

val omeletteData = RecipeData(
    name = "Omelette",
    time = 10,
    ingredients = listOf("Eggs", "Cheese", "Breadcrumbs"),
    tools = listOf("Pan", "Pot"),
    description = "A simple breakfast",
    img = "omelette"
)