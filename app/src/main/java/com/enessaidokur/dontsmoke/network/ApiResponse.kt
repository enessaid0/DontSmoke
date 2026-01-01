package com.enessaidokur.dontsmoke.network

// network/ApiResponse.kt
data class FiyatApiResponse(
    val success: Boolean,
    val rates: Map<String, Double> // Örn: "USD": 32.5, "EUR": 34.8
)
