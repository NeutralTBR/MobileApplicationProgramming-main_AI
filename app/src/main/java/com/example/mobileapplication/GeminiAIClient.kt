package com.example.mobileapplication

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    object Initial : UiState

    object Loading : UiState

    data class Success(val outputText: String) : UiState

    data class Error(val errorMessage: String) : UiState
}

class GeminiAIClient : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val model = GenerativeModel(
        "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey,
//        generationConfig = generationConfig {
//            temperature = 1f
//            topK = 40
//            topP = 0.95f
//            maxOutputTokens = 8192
//            responseMimeType = "text/plain"
//        },
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = model.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                Log.e("GeminiAIClient", "Error generating content", e)
                _uiState.value = UiState.Error(e.localizedMessage ?: "An unknown error occurred")
            }
        }
    }
}

