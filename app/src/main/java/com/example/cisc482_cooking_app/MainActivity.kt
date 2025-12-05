package com.example.cisc482_cooking_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import com.example.cisc482_cooking_app.ui.theme.Cream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CISC482CookingAppTheme {
                CollegeFridgeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeFridgeApp() {
    val navController = rememberNavController()

    // --- STATE IS HOISTED AND CORRECTED HERE ---
    // The User state now matches the User.kt data class exactly.
    var userState by remember {
        mutableStateOf(
            User(
                id = "12345", // Added required ID
                name = "John",
                email = "jtfulky@udel.edu",
                hashedPassword = "a_very_secure_placeholder_hash", // Added required password hash
                allergies = listOf( // Using an immutable List, as per User.kt
                    Allergy.SOY, Allergy.EGGS, Allergy.PEANUTS, Allergy.FISH, Allergy.SESAME,
                    Allergy.SHELLFISH, Allergy.TREE_NUTS, Allergy.MILK, Allergy.WHEAT_GLUTEN
                ),
                // customAllergy is null because Allergy.OTHER is not in the initial list.
                customAllergy = null
            )
        )
    }
    // --- END OF HOISTED STATE ---

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
            composable(Screen.Recipes.route) { RecipesScreen() }

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
