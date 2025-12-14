package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PantryViewModel : ViewModel() {
    val pantryItems = mutableStateListOf<String>()

    fun addPantryItem(item: String) {
        if (item.isNotBlank() && !pantryItems.contains(item)) {
            pantryItems.add(item)
        }
    }

    fun addPantryItems(items: List<String>) {
        items.forEach { addPantryItem(it) }
    }

    fun removePantryItem(item: String) {
        pantryItems.remove(item)
    }

    init {
        // Add initial sample data
        addPantryItems(listOf("Milk", "Eggs", "Bread", "Cheese", "Chicken Breast"))
    }
}