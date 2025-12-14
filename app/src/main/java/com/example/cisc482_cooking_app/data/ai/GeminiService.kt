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
private const val TAG = "GeminiService"
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
                        Log.e(TAG, "HTTP ${response.code} -> $errorBody")
                        return@withContext GeminiResult.Error(
                            "Gemini request failed (${response.code})",
                            errorBody
                        )
                    }

                    val body = response.body?.string() ?: return@withContext GeminiResult.Error(
                        "Gemini response body was empty"
                    )

                    Log.d(TAG, "Raw Gemini response: $body")

                    val parsedText = parseTextFromResponse(body)
                    if (parsedText != null) {
                        GeminiResult.Success(parsedText)
                    } else {
                        GeminiResult.Error("Unable to parse Gemini response", body)
                    }
                }
            } catch (ioException: IOException) {
                Log.e(TAG, "Network error", ioException)
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
        appendLine("Respond with a single JSON object EXACTLY matching this schema (no code fences, no prose, do not wrap the JSON in quotes):")
        appendLine(
            "{" +
                "\n  \"id\": \"string\"," +
                "\n  \"title\": \"string\"," +
                "\n  \"description\": \"string\"," +
                "\n  \"ingredients\": [\"string\", \"string\", \"string\"]," +
                "\n  \"tools\": [\"string\", \"string\"]," +
                "\n  \"steps\": [\"string\", \"string\", \"string\"]," +
                "\n  \"imageUrls\": [\"https://example.com/photo.jpg\"]," +
                "\n  \"totalTimeMinutes\": 30," +
                "\n  \"rating\": 4.7," +
                "\n  \"difficulty\": \"EASY|MEDIUM|HARD\"" +
                "\n}"
        )
        appendLine("Rules:")
        appendLine("1. The first non-whitespace character MUST be '{' and the last MUST be '}'.")
        appendLine("2. Do not escape quotes inside the JSON (e.g., write \"title\": \"...\", not \"\\\"title\\\"\").")
        appendLine("3. ingredients and steps must each contain at least 3 entries; tools may be empty but prefer at least 1 item.")
        appendLine("4. imageUrls must contain at least one https URL. Use this EXACT format: 'https://image.pollinations.ai/prompt/{description}', where {description} is a short visual description of the dish with spaces replaced by '%20' (e.g., 'delicious%20lemon%20pasta%20on%20plate'). Do not use generic placeholders.")
        appendLine("5. totalTimeMinutes must be a positive integer; rating must be between 0 and 5; difficulty must be exactly EASY, MEDIUM, or HARD.")
        append("Return only the JSON object with no commentary.")
    }
}

sealed interface GeminiResult<out T> {
    data class Success<T>(val data: T) : GeminiResult<T>
    data class Error(val message: String, val debugBody: String? = null) : GeminiResult<Nothing>
}
