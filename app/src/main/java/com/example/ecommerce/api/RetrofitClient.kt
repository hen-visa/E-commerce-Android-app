package com.example.ecommerce.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit client.
 * Consumed via Hilt injection (see NetworkModule.kt).
 */
object RetrofitClient {

    private const val BASE_URL = "https://fakestoreapi.com/"
    private const val TIMEOUT_SECONDS = 30L

    /** OkHttp client with logging + reasonable timeouts. */
    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /** Lazy Retrofit instance. */
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Pre-built ApiService for direct use if DI is not available. */
    val apiService: ApiService by lazy {
        instance.create(ApiService::class.java)
    }
}
