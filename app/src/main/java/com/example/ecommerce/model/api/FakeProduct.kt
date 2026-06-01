package com.example.ecommerce.model.api

import com.google.gson.annotations.SerializedName

/**
 * Maps to a single product object returned by
 * GET https://fakestoreapi.com/products
 */
data class FakeProduct(
    @SerializedName("id")          val id: Int = 0,
    @SerializedName("title")       val title: String = "",
    @SerializedName("price")       val price: Double = 0.0,
    @SerializedName("description") val description: String = "",
    @SerializedName("category")    val category: String = "",
    @SerializedName("image")       val image: String = "",
    @SerializedName("rating")      val rating: RatingModel? = null
)

data class RatingModel(
    @SerializedName("rate")  val rate: Double = 0.0,
    @SerializedName("count") val count: Int = 0
)
