package com.example.ecommerce.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.NetworkResult
import com.example.ecommerce.model.api.FakeProduct
import com.example.ecommerce.model.api.FakeUser
import com.example.ecommerce.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FakeStoreVM"

/**
 * ViewModel exposing 3 FakeStore API endpoints to the UI layer.
 *
 * Each endpoint has three states:  Loading → Success / Error
 * represented as [NetworkResult].
 */
@HiltViewModel
class FakeStoreViewModel @Inject constructor(
    private val repository: FakeStoreRepository
) : ViewModel() {

    // ── Endpoint 1 — Products ───────────────────────────────────────────────
    private val _products = MutableLiveData<NetworkResult<List<FakeProduct>>>()
    val products: LiveData<NetworkResult<List<FakeProduct>>> get() = _products

    fun loadProducts() {
        _products.value = NetworkResult.Loading
        viewModelScope.launch {
            Log.d(TAG, "loadProducts: fetching…")
            _products.value = repository.fetchProducts().also {
                Log.d(TAG, "loadProducts: result = $it")
            }
        }
    }

    // ── Endpoint 2 — Categories ─────────────────────────────────────────────
    private val _categories = MutableLiveData<NetworkResult<List<String>>>()
    val categories: LiveData<NetworkResult<List<String>>> get() = _categories

    fun loadCategories() {
        _categories.value = NetworkResult.Loading
        viewModelScope.launch {
            Log.d(TAG, "loadCategories: fetching…")
            _categories.value = repository.fetchCategories().also {
                Log.d(TAG, "loadCategories: result = $it")
            }
        }
    }

    // ── Endpoint 3 — Users ──────────────────────────────────────────────────
    private val _users = MutableLiveData<NetworkResult<List<FakeUser>>>()
    val users: LiveData<NetworkResult<List<FakeUser>>> get() = _users

    fun loadUsers() {
        _users.value = NetworkResult.Loading
        viewModelScope.launch {
            Log.d(TAG, "loadUsers: fetching…")
            _users.value = repository.fetchUsers().also {
                Log.d(TAG, "loadUsers: result = $it")
            }
        }
    }

    /** Convenience — triggers all 3 endpoints at once. */
    fun loadAll() {
        loadProducts()
        loadCategories()
        loadUsers()
    }
}
