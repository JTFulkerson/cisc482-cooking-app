package com.example.cisc482_cooking_app.navigation

sealed class Screen(val route: String) {
    object Scanner : Screen("scanner")
    object Browse : Screen("browse")
    object Recipes : Screen("recipes")
    object GenerateRecipe : Screen("generate_recipe")
    object Profile : Screen("profile")
}