package com.enessaidokur.dontsmoke.network

import retrofit2.http.GET

// Dört farklı API uç noktasını çağıran yeni arayüz
interface ApiService {

    @GET("economy/allCurrency")
    suspend fun getAllCurrencies(): AllCurrencyResponse

    @GET("economy/goldPrice")
    suspend fun getGoldPrices(): GoldPriceResponse

    @GET("economy/silverPrice")
    suspend fun getSilverPrice(): SilverPriceResponse

    @GET("economy/cripto")
    suspend fun getCryptoPrices(): CryptoResponse
}
