package com.example.ecommerce.repository

import com.example.ecommerce.api.ApiService
import com.example.ecommerce.api.NetworkResult
import com.example.ecommerce.model.api.FakeProduct
import com.example.ecommerce.model.api.FakeUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for all FakeStore REST API operations.
 * Wraps each call in a try/catch and emits [NetworkResult].
 *
 * Endpoints covered
 *  1. GET /products          → fetchProducts()
 *  2. GET /products/categories → fetchCategories()
 *  3. GET /users             → fetchUsers()
 */
@Singleton
class FakeStoreRepository @Inject constructor(
    private val apiService: ApiService
) {

    // ── Endpoint 1 — GET /products ──────────────────────────────────────────
    suspend fun fetchProducts(): NetworkResult<List<FakeProduct>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body() ?: emptyList())
                } else {
                    NetworkResult.Error(
                        message = "Server error: ${response.message()}",
                        code    = response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }

    // ── Endpoint 2 — GET /products/categories ──────────────────────────────
    suspend fun fetchCategories(): NetworkResult<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCategories()
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body() ?: emptyList())
                } else {
                    NetworkResult.Error(
                        message = "Server error: ${response.message()}",
                        code    = response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }

    // ── Endpoint 3 — GET /users ─────────────────────────────────────────────
    suspend fun fetchUsers(): NetworkResult<List<FakeUser>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUsers()
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body() ?: emptyList())
                } else {
                    NetworkResult.Error(
                        message = "Server error: ${response.message()}",
                        code    = response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
}
