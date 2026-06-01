package com.example.ecommerce.api

import com.example.ecommerce.model.api.FakeProduct
import com.example.ecommerce.model.api.FakeUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API interface for FakeStore API.
 * Provides 3 endpoints satisfying university Requirement #5.
 *
 * Base URL: https://fakestoreapi.com/
 */
interface ApiService {

    /**
     * Endpoint 1 — GET /products
     * Returns a full list of store products.
     */
    @GET("products")
    suspend fun getProducts(): Response<List<FakeProduct>>

    /**
     * Endpoint 2 — GET /products/categories
     * Returns a list of category name strings.
     */
    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

    /**
     * Endpoint 3 — GET /users
     * Returns a list of registered users.
     */
    @GET("users")
    suspend fun getUsers(): Response<List<FakeUser>>

    /**
     * Optional helper — GET /products/{id}
     * Fetches a single product by its ID.
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<FakeProduct>
}
