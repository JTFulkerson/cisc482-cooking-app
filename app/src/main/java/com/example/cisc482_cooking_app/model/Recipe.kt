package com.example.cisc482_cooking_app.model

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,

    // Now multiple images instead of one
    val imageUrls: List<String> = emptyList(),

    // Total time in minutes
    val totalTimeMinutes: Int,

    // Rating from 0–5
    val rating: Float,

    // Difficulty enum
    val difficulty: Difficulty
) {
    init {
        require(totalTimeMinutes > 0) {
            "Total time must be greater than 0 minutes"
        }

        require(rating in 0f..5f) {
            "Rating must be between 0 and 5"
        }
    }
}