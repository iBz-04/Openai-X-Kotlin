package com.example.chatinv.network

import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse
} 