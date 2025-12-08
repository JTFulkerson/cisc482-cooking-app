package com.example.cisc482_cooking_app.data.ai

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1/models"
private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

/**
 * Central place for every Gemini API interaction. Wraps the raw HTTP calls and
 * exposes higher-level suspend functions tailored to the app's use-cases.
 */
class GeminiService(
    private val apiKey: String,
    private val model: String = "gemini-2.5-flash",
    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(45, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()
) {

    suspend fun generateRecipe(request: GeminiRecipeRequest): GeminiResult<String> {
        val prompt = request.asPrompt()
        return generateText(prompt)
    }

    suspend fun generateText(prompt: String): GeminiResult<String> {
        if (apiKey.isBlank()) {
            return GeminiResult.Error("Gemini API key is missing. Add GEMINI_API_KEY to local.properties.")
        }

        val requestPayload = JSONObject(
            mapOf(
                "contents" to JSONArray(
                    listOf(
                        JSONObject(
                            mapOf(
                                "parts" to JSONArray(
                                    listOf(
                                        JSONObject(mapOf("text" to prompt))
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        val httpRequest = Request.Builder()
            .url("$GEMINI_BASE_URL/$model:generateContent?key=$apiKey")
            .post(requestPayload.toString().toRequestBody(JSON_MEDIA_TYPE))
            .build()

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(httpRequest).execute().use { response ->
                    if (!response.isSuccessful) {
                        val errorBody = response.body?.string()
                        Log.e("GeminiService", "HTTP ${response.code} -> $errorBody")
                        return@withContext GeminiResult.Error(
                            "Gemini request failed (${response.code})",
                            errorBody
                        )
                    }

                    val body = response.body?.string() ?: return@withContext GeminiResult.Error(
                        "Gemini response body was empty"
                    )

                    val parsedText = parseTextFromResponse(body)
                    if (parsedText != null) {
                        GeminiResult.Success(parsedText)
                    } else {
                        GeminiResult.Error("Unable to parse Gemini response", body)
                    }
                }
            } catch (ioException: IOException) {
                Log.e("GeminiService", "Network error", ioException)
                GeminiResult.Error("Network error talking to Gemini", ioException.message)
            }
        }
    }

    private fun parseTextFromResponse(rawBody: String): String? {
        return try {
            val root = JSONObject(rawBody)
            val candidates = root.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            if (parts.length() == 0) return null
            parts.getJSONObject(0).optString("text")
        } catch (_: JSONException) {
            null
        }
    }
}

/** Simple in-memory repository so the rest of the app never touches HTTP details. */
class GeminiRepository(private val service: GeminiService) {

    suspend fun generateRecipeFromSelections(request: GeminiRecipeRequest): GeminiResult<String> =
        service.generateRecipe(request)
}

/** Convenience wrapper for Gemini prompts tailored to recipe generation. */
data class GeminiRecipeRequest(
    val ingredients: List<String>,
    val supplies: List<String>,
    val allergies: List<String>,
    val customRequest: String? = null
) {
    fun asPrompt(): String = buildString {
        appendLine("You are an AI chef. Create a detailed recipe.")
        if (ingredients.isNotEmpty()) {
            appendLine("Available ingredients: ${ingredients.joinToString()}")
        }
        if (supplies.isNotEmpty()) {
            appendLine("Available supplies/equipment: ${supplies.joinToString()}")
        }
        if (allergies.isNotEmpty()) {
            appendLine("Allergy considerations: ${allergies.joinToString()}")
        }
        if (!customRequest.isNullOrBlank()) {
            appendLine("Additional request: ${customRequest.trim()}")
        }
        append("Return a JSON object with title, servings, prep_time, cook_time, ingredients, and steps.")
    }
}

sealed interface GeminiResult<out T> {
    data class Success<T>(val data: T) : GeminiResult<T>
    data class Error(val message: String, val debugBody: String? = null) : GeminiResult<Nothing>
}
