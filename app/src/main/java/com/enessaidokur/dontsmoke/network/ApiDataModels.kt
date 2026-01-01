package com.enessaidokur.dontsmoke.network

// --- DÖVİZ KURLARI İÇİN ---
data class AllCurrencyResponse(
    val success: Boolean,
    val result: List<CurrencyResult>
)
data class CurrencyResult(
    val name: String,
    val buying: String?,
    val selling: String?
)

// --- ALTIN FİYATLARI İÇİN (Logcat'e göre düzeltildi) ---
data class GoldPriceResponse(
    val success: Boolean,
    val result: List<GoldPriceResult>
)
data class GoldPriceResult(
    val name: String,
    val buying: Double?, // Alan adı ve türü düzeltildi (buy -> buying, String -> Double?)
    val selling: Double? // Alan adı ve türü düzeltildi (sell -> selling, String -> Double?)
)

// --- GÜMÜŞ FİYATLARI İÇİN ---
data class SilverPriceResponse(
    val success: Boolean,
    val result: SilverPriceResult
)
data class SilverPriceResult(
    val buying: Double,
    val selling: Double
)

// --- KRİPTO PARALAR İÇİN ---
data class CryptoResponse(
    val success: Boolean,
    val result: List<CryptoResult>
)
data class CryptoResult(
    val code: String,
    val name: String,
    val price: Double
)
