package com.example.ecommerce.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecommerce.model.BrandModel
import com.example.ecommerce.model.ItemsModel
import com.example.ecommerce.model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val _brands   = MutableLiveData<MutableList<BrandModel>>()
    private val _banners  = MutableLiveData<List<SliderModel>>()
    private val _popular  = MutableLiveData<MutableList<ItemsModel>>()
    private val _error    = MutableLiveData<String>()

    val brands:  LiveData<MutableList<BrandModel>> get() = _brands
    val banners: LiveData<List<SliderModel>>       get() = _banners
    val popular: LiveData<MutableList<ItemsModel>> get() = _popular
    val error:   LiveData<String>                  get() = _error

    fun loadBrands() {
        firebaseDatabase.getReference("Category")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _brands.value = snapshot.children
                        .mapNotNull { it.getValue(BrandModel::class.java) }
                        .toMutableList()
                }
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load categories: ${error.message}"
                }
            })
    }

    fun loadBanners() {
        firebaseDatabase.getReference("Banner")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _banners.value = snapshot.children
                        .mapNotNull { it.getValue(SliderModel::class.java) }
                }
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load banners: ${error.message}"
                }
            })
    }

    fun loadPopular() {
        firebaseDatabase.getReference("Items")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _popular.value = snapshot.children
                        .mapNotNull { it.getValue(ItemsModel::class.java) }
                        .toMutableList()
                }
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load items: ${error.message}"
                }
            })
    }

    fun loadByCategory(categoryId: Int): LiveData<MutableList<ItemsModel>> {
        val result = MutableLiveData<MutableList<ItemsModel>>()
        firebaseDatabase.getReference("Items")
            .orderByChild("categoryId")
            .equalTo(categoryId.toDouble())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    result.value = snapshot.children
                        .mapNotNull { it.getValue(ItemsModel::class.java) }
                        .toMutableList()
                }
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load [resource]: ${error.message}"
                }
            })
        return result
    }
}