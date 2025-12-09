package com.example.cisc482_cooking_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cisc482_cooking_app.data.ai.GeminiRepository
import com.example.cisc482_cooking_app.data.ai.GeminiService
import com.example.cisc482_cooking_app.ui.screens.*
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object GenerateRecipe : Screen("generate_recipe", "Generator", { Icon(Icons.Default.Home, contentDescription = null) })
    object Scanner : Screen("scanner", "Scanner", { Icon(Icons.Default.CameraAlt, contentDescription = null) })
    object SocialFeed : Screen("social_feed", "Social", { Icon(Icons.Default.Share, contentDescription = null) })
    object MyRecipes : Screen("my_recipes", "My Recipes", { Icon(Icons.Default.Book, contentDescription = null) })
    object UserProfile : Screen("user_profile", "Profile", { Icon(Icons.Default.Person, contentDescription = null) })
}

object RecipeDetailDest {
    const val route = "recipe_detail/{recipeId}"
    fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CISC482CookingAppTheme {
                val geminiRepository = remember {
                    GeminiRepository(GeminiService(BuildConfig.GEMINI_API_KEY))
                }
                val navController = rememberNavController()
                val items = listOf(Screen.GenerateRecipe, Screen.Scanner, Screen.SocialFeed, Screen.MyRecipes, Screen.UserProfile)
                val context = LocalContext.current
                var myRecipes by rememberSaveable { mutableStateOf(emptyList<RecipeDetails>()) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = screen.icon,
                                    label = { Text(screen.label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.GenerateRecipe.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.GenerateRecipe.route) {
                            GenerateRecipeScreen(
                                ingredientOptions = listOf("All-purpose Flour", "..."),
                                supplyOptions = listOf("Baking Sheet", "..."),
                                generateRecipe = { request ->
                                    geminiRepository.generateRecipeFromSelections(request)
                                }
                            )
                        }
                        composable(Screen.Scanner.route) {
                            ScannerScreen()
                        }
                        composable(Screen.SocialFeed.route) {
                            SocialFeedScreen(posts = sampleFeed) {
                                navController.navigate(RecipeDetailDest.createRoute(it))
                            }
                        }
                        composable(Screen.MyRecipes.route) {
                            MyRecipesScreen(recipes = myRecipes) {
                                navController.navigate(RecipeDetailDest.createRoute(it))
                            }
                        }
                        composable(Screen.UserProfile.route) {
                            UserProfileScreen(user = sampleUserProfile) {
                                navController.navigate(RecipeDetailDest.createRoute(it))
                            }
                        }
                        composable(
                            route = RecipeDetailDest.route,
                            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId")
                            val recipe = allSampleRecipes.find { it.id == recipeId }
                            RecipeDetailScreen(recipe = recipe) { rId ->
                                val recipeToImport = allSampleRecipes.find { it.id == rId }
                                if (recipeToImport != null && myRecipes.none { it.id == rId }) {
                                    myRecipes = myRecipes + recipeToImport
                                    Toast.makeText(context, "Recipe imported!", Toast.LENGTH_SHORT).show()
                                } else if (recipeToImport != null) {
                                    Toast.makeText(context, "Recipe already in your list.", Toast.LENGTH_SHORT).show()
                                }
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}