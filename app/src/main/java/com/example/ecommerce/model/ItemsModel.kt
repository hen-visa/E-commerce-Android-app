package com.example.ecommerce.model

import java.io.Serializable

data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var size: ArrayList<String> = ArrayList(),
    var color: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var oldPrice: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 1,
    var categoryId: Int = 0,       // ADD: matches BrandModel.id
    var stockCount: Int = 99,      // ADD: for Feature 7 stock management
) : Serializable
