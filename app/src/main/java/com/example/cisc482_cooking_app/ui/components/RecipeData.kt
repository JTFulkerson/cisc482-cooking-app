package com.example.cisc482_cooking_app.ui.components

class RecipeData(
    val name: String,
    val time: Int,
    val ingredients: List<String>,
    val description: String
) {

}

val pBJData = RecipeData(
    name = "Peanut Butter & Jelly",
    time = 5,
    ingredients = listOf("Peanut Butter", "Jelly", "Bread"),
    description = "A classic school lunch"
)