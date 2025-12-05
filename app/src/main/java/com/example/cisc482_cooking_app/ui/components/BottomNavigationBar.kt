package com.example.cisc482_cooking_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // <-- Import FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cisc482_cooking_app.navigation.Screen
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
// Import your NavBackground color
import com.example.cisc482_cooking_app.ui.theme.NavBackground

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Scanner,
        Screen.Browse,
        Screen.Recipes,
        Screen.Profile
    )
    // Using filled icons for a bolder look, as seen in the image
    val icons = listOf(
        Icons.Default.QrCodeScanner,
        Icons.Default.Search,
        Icons.Default.MenuBook,
        Icons.AutoMirrored.Filled.List
    )
    val labels = listOf("Scanner", "Browse", "Recipes", "Pantry")

    // The Surface now acts as the floating, rounded container for the whole bar
    Surface(
        color = NavBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEachIndexed { index, screen ->
                val isSelected = currentRoute == screen.route
                val backgroundColor = if (isSelected) EspressoBrown else Color.Transparent
                val contentColor = if (isSelected) Cream else EspressoBrown

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(backgroundColor)
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = labels[index],
                        tint = contentColor
                    )
                    // --- THIS IS THE CHANGED LINE ---
                    Text(
                        text = labels[index],
                        color = contentColor,
                        fontWeight = FontWeight.Bold // Make the text bold
                    )
                    // --- END OF CHANGE ---
                }
            }
        }
    }
}

// ... (The rest of your BottomNavigationBar.kt file is correct and stays the same) ...

@Preview(showBackground = true, backgroundColor = 0xFFFFF7E1) // Use Cream color hex for preview
@Composable
fun BottomNavigationBarPreview() {
    CISC482CookingAppTheme {
        // Wrap the component in a Surface with the app's background color
        // to accurately represent how it looks in the app.
        Surface(color = Cream) {
            BottomNavigationBar(navController = rememberNavController())
        }
    }
}

