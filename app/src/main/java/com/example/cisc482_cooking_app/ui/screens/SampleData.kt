package com.example.cisc482_cooking_app.ui.screens

// 1. Consolidated Recipe Details
val allSampleRecipes = listOf(
    RecipeDetails(
        id = "1",
        title = "Classic Tomato Bruschetta",
        imageUrl = "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("4 medium tomatoes, diced", "1/4 cup fresh basil, chopped", "2 cloves garlic, minced", "1 tbsp balsamic vinegar", "1 tbsp olive oil", "Salt and pepper to taste", "1 baguette, sliced"),
        instructions = "1. In a bowl, combine tomatoes, basil, garlic, balsamic vinegar, and olive oil. Season with salt and pepper.\n2. Toast the baguette slices until golden brown.\n3. Top each slice with the tomato mixture and serve immediately."
    ),
    RecipeDetails(
        id = "2",
        title = "Pancakes with Berries",
        imageUrl = "https://images.pexels.com/photos/376464/pexels-photo-376464.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("1 cup all-purpose flour", "2 tbsp sugar", "2 tsp baking powder", "1/2 tsp salt", "1 egg", "1 cup milk", "2 tbsp melted butter", "Mixed berries for topping"),
        instructions = "1. Mix dry ingredients. In a separate bowl, mix egg, milk, and butter. \n2. Combine wet and dry ingredients. \n3. Pour batter onto a hot griddle and cook until golden brown on both sides. \n4. Serve with berries."
    ),
    RecipeDetails(
        id = "3",
        title = "Classic Cheeseburger",
        imageUrl = "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("1/2 lb ground beef", "1 hamburger bun", "1 slice cheddar cheese", "Lettuce, tomato, onion", "Ketchup, mustard"),
        instructions = "1. Form beef into a patty and cook on a grill or pan to desired doneness. \n2. Melt cheese on top. \n3. Serve on a bun with toppings."
    ),
    RecipeDetails(
        id = "4",
        title = "Grilled Fish",
        imageUrl = "https://images.pexels.com/photos/262978/pexels-photo-262978.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("1 fish fillet", "1 tbsp olive oil", "1 lemon", "Salt, pepper, dill"),
        instructions = "1. Season fish with salt, pepper, and dill. \n2. Grill on medium-high heat for 4-6 minutes per side. \n3. Squeeze fresh lemon juice over the top before serving."
    ),
    RecipeDetails(
        id = "5",
        title = "Steak Burrito",
        imageUrl = "https://images.pexels.com/photos/461198/pexels-photo-461198.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("1/2 lb sliced steak", "1 large flour tortilla", "Rice, beans, salsa, cheese, sour cream"),
        instructions = "1. Cook steak slices. \n2. Warm tortilla. \n3. Assemble all ingredients inside the tortilla and roll it up."
    ),
    RecipeDetails(
        id = "6",
        title = "Fruit Bowl",
        imageUrl = "https://images.pexels.com/photos/1099680/pexels-photo-1099680.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        ingredients = listOf("Strawberries, blueberries, kiwi, mango, banana"),
        instructions = "1. Chop all fruits. \n2. Combine in a bowl. \n3. Enjoy!"
    )
)

// 2. Updated UserProfile to use Recipe IDs
val sampleUserProfile = UserProfile(
    name = "Jane Doe",
    profileImageUrl = "https://randomuser.me/api/portraits/women/75.jpg",
    sharedRecipes = listOf("1", "2", "3", "4", "5", "6") // Now uses IDs
)

// 3. Updated Social Feed to use consistent data
val sampleFeed = listOf(
    RecipePost(
        id = "1",
        user = PostUser("John Doe", "https://randomuser.me/api/portraits/men/75.jpg"),
        imageUrl = allSampleRecipes[0].imageUrl,
        caption = "Just made this delicious salad! So refreshing and healthy. #healthyeating #salad #recipe"
    ),
    RecipePost(
        id = "2",
        user = PostUser("Alice Smith", "https://randomuser.me/api/portraits/women/44.jpg"),
        imageUrl = allSampleRecipes[1].imageUrl,
        caption = "Pancakes for breakfast, the perfect start to the day. Who wants some? 🥞"
    ),
    RecipePost(
        id = "3",
        user = PostUser("Bob Johnson", "https://randomuser.me/api/portraits/men/32.jpg"),
        imageUrl = allSampleRecipes[2].imageUrl,
        caption = "Burger and fries, a classic combo that never gets old. #burger #fries #food"
    ),
    RecipePost(
        id = "4",
        user = PostUser("Clara White", "https://randomuser.me/api/portraits/women/65.jpg"),
        imageUrl = allSampleRecipes[3].imageUrl,
        caption = "Fresh grilled fish for dinner tonight. Absolutely delicious and so simple to make."
    ),
    RecipePost(
        id = "5",
        user = PostUser("David Green", "https://randomuser.me/api/portraits/men/55.jpg"),
        imageUrl = allSampleRecipes[4].imageUrl,
        caption = "Can't beat a giant steak burrito after a long day."
    ),
    RecipePost(
        id = "6",
        user = PostUser("Eva Black", "https://randomuser.me/api/portraits/women/33.jpg"),
        imageUrl = allSampleRecipes[5].imageUrl,
        caption = "Starting the morning right with a colorful fruit bowl."
    )
)
