package com.enessaidokur.dontsmoke.network.yapayzeka

import com.google.gson.annotations.SerializedName

// API'ye gönderilecek olan isteğin yapısı
data class ChatRequest(
    val model: String,
    val messages: List<Message>
)

// API'den gelen cevabın yapısı
data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

// Hem istekte hem de cevapta kullanılan mesaj modeli
data class Message(
    val role: String, // "user", "assistant", veya "system"
    val content: String
)
