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
import androidx.compose.material.icons.filled.MenuBook // Import MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cisc482_cooking_app.navigation.Screen
import com.example.cisc482_cooking_app.ui.theme.CISC482CookingAppTheme
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Scanner,
        Screen.Browse,
        Screen.Recipes,
        Screen.Profile
    )
    val icons = listOf(
        Icons.Filled.QrCodeScanner,
        Icons.Default.Search,
        Icons.Filled.MenuBook, // Use MenuBook instead of Book
        Icons.AutoMirrored.Filled.List
    )
    val labels = listOf("Scanner", "Browse", "Recipes", "Pantry")

    Surface(
        color = Cream,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEachIndexed { index, screen ->
                val isSelected = currentRoute == screen.route
                val backgroundColor = if (isSelected) EspressoBrown else LightGray
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
                    Text(text = labels[index], color = contentColor)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    CISC482CookingAppTheme {
        BottomNavigationBar(navController = rememberNavController())
    }
}
