package com.enessaidokur.dontsmoke.network.yapayzeka

import com.enessaidokur.dontsmoke.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YapayZekaRetrofitInstance {

    private const val BASE_URL = "https://openrouter.ai/api/v1/"

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.OPENROUTER_API_KEY}")
                    // OpenRouter'ın tavsiye ettiği başlıkları ekliyoruz.
                    .addHeader("HTTP-Referer", BuildConfig.APPLICATION_ID)
                    .addHeader("X-Title", "DontSmoke")
                    .build()
                chain.proceed(request)
            }
            .also {
                if (BuildConfig.DEBUG) {
                    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    it.addInterceptor(logger)
                }
            }
            .build()
    }

    val api: YapayZekaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YapayZekaApiService::class.java)
    }
}
