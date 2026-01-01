package com.enessaidokur.dontsmoke.network

// Yeni API'den (collectapi) gelen JSON yapısına uygun veri sınıfları

data class CollectApiResponse(
    val success: Boolean,
    val result: ApiResult
)

data class ApiResult(
    val base: String,
    val lastupdate: String,
    val data: List<CurrencyData>
)

data class CurrencyData(
    val code: String,
    val name: String,
    val rate: Double
)
