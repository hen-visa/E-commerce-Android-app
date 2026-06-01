package com.example.ecommerce.model.api

import com.google.gson.annotations.SerializedName

/**
 * Maps to a user object returned by
 * GET https://fakestoreapi.com/users
 */
data class FakeUser(
    @SerializedName("id")       val id: Int = 0,
    @SerializedName("email")    val email: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("name")     val name: UserName? = null,
    @SerializedName("phone")    val phone: String = "",
    @SerializedName("address")  val address: UserAddress? = null
)

data class UserName(
    @SerializedName("firstname") val firstName: String = "",
    @SerializedName("lastname")  val lastName: String = ""
) {
    val fullName: String get() = "$firstName $lastName".trim()
}

data class UserAddress(
    @SerializedName("city")   val city: String = "",
    @SerializedName("street") val street: String = "",
    @SerializedName("number") val number: Int = 0,
    @SerializedName("zipcode") val zipcode: String = ""
)
