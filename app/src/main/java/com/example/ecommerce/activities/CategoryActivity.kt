package com.example.ecommerce.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapters.PopularAdapter
import com.example.ecommerce.databinding.ActivityCategoryBinding
import com.example.ecommerce.model.ItemsModel
import com.example.ecommerce.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryId = intent.getIntExtra("categoryId", -1)
        val categoryTitle = intent.getStringExtra("categoryTitle") ?: "Category"

        if (categoryId == -1) {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.titleTxt.text = categoryTitle
        binding.backBtn.setOnClickListener { finish() }

        val adapter = PopularAdapter(mutableListOf())
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerItems.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE
        binding.emptyTxt.visibility = View.GONE

        // Observe category items with explicit type to fix inference errors
        viewModel.loadByCategory(categoryId).observe(this) { items: MutableList<ItemsModel>? ->
            binding.progressBar.visibility = View.GONE
            if (items.isNullOrEmpty()) {
                binding.emptyTxt.visibility = View.VISIBLE
                binding.recyclerItems.visibility = View.GONE
            } else {
                binding.emptyTxt.visibility = View.GONE
                binding.recyclerItems.visibility = View.VISIBLE
                // Fixed: Renamed from updateDate to updateData to match PopularAdapter
                adapter.updateData(items)
            }
        }

        viewModel.error.observe(this) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        }
    }
}
