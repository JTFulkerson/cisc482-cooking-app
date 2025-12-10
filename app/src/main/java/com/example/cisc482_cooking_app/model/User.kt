package com.example.cisc482_cooking_app.model

// Strongly typed allergies
enum class Allergy {
    SOY,
    SHELLFISH,
    EGGS,
    TREE_NUTS,
    PEANUTS,
    MILK,
    FISH,
    WHEAT,
    GLUTEN,
    SESAME,
    OTHER
}

data class User(
    val id: String,
    val name: String,
    val email: String,

    // Store a hash, not plaintext
    val hashedPassword: String,

    val profilePictureUrl: String? = null,

    // Predefined allergies
    val allergies: List<Allergy> = emptyList(),

    // Custom allergy text ONLY when Allergy.OTHER is in allergies
    val customAllergy: String? = null,

    // List of saved recipes
    val savedRecipes: List<Recipe> = emptyList()
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }

        require(email.contains("@") && email.contains(".")) {
            "Email must be valid"
        }

        require(hashedPassword.isNotBlank()) {
            "Password hash cannot be empty"
        }

        // Validation for custom allergy
        if (allergies.contains(Allergy.OTHER)) {
            require(!customAllergy.isNullOrBlank()) {
                "Custom allergy must be provided when 'OTHER' is selected"
            }
        } else {
            require(customAllergy == null) {
                "Custom allergy must be null unless 'OTHER' is selected"
            }
        }
    }
}