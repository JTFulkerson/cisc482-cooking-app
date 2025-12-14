package com.example.cisc482_cooking_app.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.cisc482_cooking_app.data.InMemoryDb

class PantryViewModel : ViewModel() {
    private val _pantryItems = mutableStateListOf<String>()
    val pantryItems: SnapshotStateList<String> = _pantryItems

    fun addPantryItem(item: String) {
        val normalized = item.trim()
        if (normalized.isEmpty()) return
        if (_pantryItems.contains(normalized)) return

        _pantryItems.add(0, normalized)
        InMemoryDb.addPantryItem(normalized)
    }

    fun addPantryItems(items: List<String>) {
        items.asReversed().forEach { addPantryItem(it) }
    }

    fun removePantryItem(item: String) {
        if (_pantryItems.remove(item)) {
            InMemoryDb.removePantryItem(item)
        }
    }

    init {
        val persistedItems = InMemoryDb.getPantryItems()
        _pantryItems.addAll(persistedItems)
    }
}