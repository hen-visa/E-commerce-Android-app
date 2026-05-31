package com.example.ecommerce.helpers

import android.content.Context
import com.example.ecommerce.model.ItemsModel

class WishlistManager(context: Context) {
    private val tinyDB = TinyDB(context)
    private val KEY = "WishList"

    fun toggle(item: ItemsModel): Boolean {
        val list = getList()
        val exists = list.any { it.title == item.title }
        if (exists) list.removeAll { it.title == item.title } else list.add(item)
        tinyDB.putListObject(KEY, list)
        return !exists  // returns true if now added
    }

    fun isFavorite(item: ItemsModel): Boolean =
        getList().any { it.title == item.title }

    fun getList(): ArrayList<ItemsModel> =
        tinyDB.getListObject(KEY) ?: arrayListOf()
}