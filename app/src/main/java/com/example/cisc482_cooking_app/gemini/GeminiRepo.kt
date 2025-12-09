
package com.example.cisc482_cooking_app.gemini

import android.graphics.Bitmap
import com.example.cisc482_cooking_app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiRepo {
    suspend fun getIngredients(image: Bitmap): String {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = BuildConfig.GEMINI_API_KEY
        )

        val inputContent = content {
            image(image)
            text("What ingredients are in this image? Give me a comma separated list")
        }

        val response = withContext(Dispatchers.IO) {
            generativeModel.generateContent(inputContent)
        }

        return response.text ?: ""
    }
}
