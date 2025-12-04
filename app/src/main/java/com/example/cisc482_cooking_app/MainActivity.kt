package com.example.cisc482_cooking_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cisc482_cooking_app.navigation.Screen
import com.example.cisc482_cooking_app.ui.components.BottomNavigationBar
import com.example.cisc482_cooking_app.ui.screens.BrowseScreen
import com.example.cisc482_cooking_app.ui.screens.ProfileScreen
import com.example.cisc482_cooking_app.ui.screens.RecipesScreen
import com.example.cisc482_cooking_app.ui.screens.ScannerScreen
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import com.example.cisc482_cooking_app.ui.theme.Cream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CISC482CookingAppTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
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
                    IconButton(onClick = { /* Handle navigation icon click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Scanner.route) { ScannerScreen() }
            composable(Screen.Browse.route) { BrowseScreen() }
            composable(Screen.Recipes.route) { RecipesScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}