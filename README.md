# College Fridge - Cooking App

"College Fridge" is a Jetpack Compose-based Android application designed to help users, particularly college students, manage their pantry, discover new recipes, and make cooking simple and accessible.

## ✨ Features

- **Recipe Discovery**: Browse a list of recipes that can be expanded to show more details.
- **Comprehensive Recipe View**: View detailed recipe information, including cooking time, ingredients, tools, and step-by-step instructions.
- **Dynamic Navigation**: A full navigation graph built with Jetpack Navigation Compose allows users to move seamlessly between screens.
- **Pantry Management**:  A dedicated screen to manage pantry items.
- **AI-Powered Recipe Generation**: Utilizes a generative AI model to create new recipes based on user selections.
- **User Profiles**: Manage user information and dietary restrictions, such as allergies.
- **Ingredient Scanner**:  A camera-based feature to scan ingredients.

## 🛠️ Technologies Used

- **UI**: 100% built with [Jetpack Compose](https://developer.android.com/jetpack/compose), using Material 3 for modern UI components.
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for all in-app navigation.
- **Asynchronous Operations**: Kotlin Coroutines for managing background threads.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for loading images asynchronously.
- **AI**: [Google Generative AI for Android](https://ai.google.dev/docs/android) to power the recipe generation feature.
- **Camera**: [CameraX](https://developer.android.com/training/camerax) for camera-related functionalities.

## 🚀 Setup and Installation

1.  Clone the repository:
    ```bash
    git clone https://github.com/your-username/cisc482-cooking-app.git
    ```
2.  Open the project in Android Studio.
3.  Add your Gemini API key in the `local.properties` file:
    ```properties
    GEMINI_API_KEY="YOUR_API_KEY"
    ```
4.  Build and run the application on an Android emulator or a physical device.

