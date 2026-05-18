package com.example.ecommerce.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.model.BrandModel
import com.example.ecommerce.model.ItemModel
import com.example.ecommerce.model.SliderModel
import com.example.ecommerce.repository.MainRepository

class MainViewModel:  ViewModel() {
    private val repository = MainRepository()

    val brands: LiveData<MutableList<BrandModel>> = repository.brands
    val banners: LiveData<List<SliderModel>> = repository.banners
    val popular: LiveData<MutableList<ItemModel>> = repository.popular


    fun loadBrands() = repository.loadBrands()
    fun loadBanners() = repository.loadBanners()
    fun loadPopular() = repository.loadPopular()
}