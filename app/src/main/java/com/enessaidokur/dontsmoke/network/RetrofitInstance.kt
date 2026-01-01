package com.enessaidokur.dontsmoke.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Yeni eklenen import
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.collectapi.com/"

    // Hata ayıklama için logging interceptor oluştur
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Tüm detayları logla
    }

    // API anahtarını ve loglamayı her isteğe eklemek için bir client oluşturuyoruz.
    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("authorization", "apikey 773CnaGC4UZJt7RFbMbMir:09dJa7JERDjPeV429bqRza")
                .addHeader("content-type", "application/json")
                .build()
            chain.proceed(request)
        }
        // Loglama interceptor'ını client'a ekle
        addInterceptor(loggingInterceptor)
    }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Özel client'ımızı Retrofit'e ekliyoruz.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
