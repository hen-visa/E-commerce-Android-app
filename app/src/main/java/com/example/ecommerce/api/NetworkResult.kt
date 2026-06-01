package com.example.ecommerce.api

/**
 * Generic sealed class representing the three API states:
 *  - Loading  : request in-flight
 *  - Success  : data returned
 *  - Error    : network or server error
 *
 * Used by every ViewModel that consumes REST data.
 */
sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
}
