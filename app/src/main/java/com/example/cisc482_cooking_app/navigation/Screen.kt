package com.example.cisc482_cooking_app.navigation

sealed class Screen(val route: String) {
    object Scanner : Screen("scanner")
    object Browse : Screen("browse")
    object Recipes : Screen("recipes")
    object GenerateRecipe : Screen("generate_recipe")
    object GeneratedRecipe : Screen("generated_recipe")
    object Pantry : Screen("pantry")
    object Profile : Screen("profile")
    object ComprehensiveRecipe : Screen("comprehensive_recipe")
}