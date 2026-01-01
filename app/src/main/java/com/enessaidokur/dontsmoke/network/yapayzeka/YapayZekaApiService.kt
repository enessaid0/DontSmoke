package com.enessaidokur.dontsmoke.network.yapayzeka

import retrofit2.http.Body
import retrofit2.http.POST

interface YapayZekaApiService {

    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse

}
