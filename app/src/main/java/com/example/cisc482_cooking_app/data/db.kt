package com.example.cisc482_cooking_app.data

import com.example.cisc482_cooking_app.model.Difficulty
import com.example.cisc482_cooking_app.model.Recipe
import com.example.cisc482_cooking_app.model.User

object InMemoryDb {
	private val recipeStore = LinkedHashMap<String, Recipe>()
	private val userStore = LinkedHashMap<String, User>()
	private val lock = Any()

	fun storeRecipe(recipe: Recipe): Recipe = synchronized(lock) {
		recipeStore[recipe.id] = recipe
		recipe
	}

	fun getRecipe(id: String): Recipe? = synchronized(lock) { recipeStore[id] }

	fun getRecipes(): List<Recipe> = synchronized(lock) { recipeStore.values.toList() }

	fun getUser(id: String): User? = synchronized(lock) { userStore[id] }

	fun create(user: User): Boolean = synchronized(lock) {
		if (userStore.containsKey(user.id)) return false
		userStore[user.id] = user
		true
	}

	fun read(id: String): User? = getUser(id)

	fun update(user: User): Boolean = synchronized(lock) {
		if (!userStore.containsKey(user.id)) return false
		userStore[user.id] = user
		true
	}

	fun delete(id: String): Boolean = synchronized(lock) { userStore.remove(id) != null }

	fun getUsers(): List<User> = synchronized(lock) { userStore.values.toList() }

	fun clearAll() = synchronized(lock) {
		recipeStore.clear()
		userStore.clear()
	}

	fun seedData() = synchronized(lock) {
		if (recipeStore.isNotEmpty()) return

		val recipeSeeds = listOf(
			Recipe(
				id = "recipe_pbj",
				title = "Peanut Butter & Jelly",
				description = "A classic school lunch",
				ingredients = listOf("Peanut Butter", "Jelly", "Bread"),
				tools = listOf("Toaster", "Knife"),
				steps = listOf(
					"Lightly toast the bread slices for crunch.",
					"Spread peanut butter evenly on one slice using the knife.",
					"Add jelly to the second slice, press together, and slice diagonally."
				),
				imageUrls = listOf(
					"https://static01.nyt.com/images/2024/09/27/multimedia/AS-Griddled-PBJ-qljg/AS-Griddled-PBJ-qljg-googleFourByThree.jpg"
				),
				totalTimeMinutes = 5,
				rating = 4.6f,
				difficulty = Difficulty.EASY
			),
			Recipe(
				id = "recipe_tacos",
				title = "Tacos",
				description = "A popular Mexican dish",
				ingredients = listOf("Tortillas", "Ground Beef", "Lettuce", "Salsa", "Sour Cream"),
				tools = listOf("Skillet", "Oven"),
				steps = listOf(
					"Brown the ground beef in a skillet and season generously.",
					"Warm the tortillas in the oven until pliable.",
					"Assemble tacos with beef, lettuce, salsa, and sour cream."
				),
				imageUrls = listOf(
					"https://marleyspoon.com/media/recipes/252043/main_photos/large/20-9fc2acc022a5fe4dbdb05419f01eca41.jpeg"
				),
				totalTimeMinutes = 30,
				rating = 4.8f,
				difficulty = Difficulty.MEDIUM
			),
			Recipe(
				id = "recipe_omelette",
				title = "Omelette",
				description = "A simple breakfast",
				ingredients = listOf("Eggs", "Cheese", "Breadcrumbs"),
				tools = listOf("Mixing Bowl", "Pan", "Spatula"),
				steps = listOf(
					"Whisk eggs with a pinch of salt in the bowl.",
					"Melt butter in a hot pan and pour in the eggs.",
					"When nearly set, add cheese and breadcrumbs, fold, and serve."
				),
				imageUrls = listOf(
					"https://www.healthyfood.com/wp-content/uploads/2018/02/Basic-omelette.jpg"
				),
				totalTimeMinutes = 10,
				rating = 4.4f,
				difficulty = Difficulty.EASY
			)
		)

		recipeSeeds.forEach { recipe ->
			recipeStore[recipe.id] = recipe
		}

		if (userStore.isEmpty()) {
			val demoUser = User(
				id = "f996ece8-009f-4454-b7de-91dee1c8f218",
				name = "John Fulkerson",
				email = "jtfulky@udel.edu",
				hashedPassword = "hashed_password",
				profilePictureUrl = "https://i.pravatar.cc/150?img=12",
				allergies = emptyList(),
				customAllergy = null,
				savedRecipes = recipeSeeds.take(2)
			)
			userStore[demoUser.id] = demoUser
		}
	}
}