package com.example.chatinv.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatRepository {
    suspend fun sendMessage(apiKey: String, messages: List<ChatMessage>): ChatResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenAiApi::class.java)
        val request = ChatRequest(model = "gpt-3.5-turbo", messages = messages)
        return api.getChatCompletion(request)
    }
} 