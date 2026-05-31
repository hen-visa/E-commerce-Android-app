package com.example.ecommerce.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.model.BrandModel
import com.example.ecommerce.model.ItemsModel
import com.example.ecommerce.model.SliderModel
import com.example.ecommerce.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    // ── Raw LiveData from Firebase ──────────────────────────────────────────
    val brands: LiveData<MutableList<BrandModel>> = repository.brands
    val banners: LiveData<List<SliderModel>> = repository.banners
    val popular: LiveData<MutableList<ItemsModel>> = repository.popular
    val error: LiveData<String> = repository.error

    // ── Search query state ──────────────────────────────────────────────────
    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // ── Filtered list — reacts to both the full list and the query ──────────
    val filteredPopular: LiveData<List<ItemsModel>> =
        MediatorLiveData<List<ItemsModel>>().apply {

            fun refilter() {
                val query = _searchQuery.value.orEmpty().trim().lowercase()
                val allItems = popular.value ?: emptyList()
                value = if (query.isEmpty()) {
                    allItems
                } else {
                    allItems.filter { item ->
                        item.title.lowercase().contains(query) ||
                                item.description.lowercase().contains(query)
                    }
                }
            }

            // Re-run the filter whenever either source changes
            addSource(popular) { refilter() }
            addSource(_searchQuery) { refilter() }
        }

    // ── Load triggers ───────────────────────────────────────────────────────
    fun loadBrands() = repository.loadBrands()
    fun loadBanners() = repository.loadBanners()
    fun loadPopular() = repository.loadPopular()
    fun loadByCategory(categoryId: Int): LiveData<MutableList<ItemsModel>> = 
        repository.loadByCategory(categoryId)
}
