package com.example.ecommerce.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.model.BrandModel
import com.example.ecommerce.model.SliderModel
import com.example.ecommerce.repository.MainRepository

class MainViewModel:  ViewModel() {
    private val repository = MainRepository()

    val brands: LiveData<MutableList<BrandModel>> = repository.brands
    val banners: LiveData<List<SliderModel>> = repository.banners


    fun loadBrands() = repository.loadBrands()
    fun loadBanners() = repository.loadBanners()
}