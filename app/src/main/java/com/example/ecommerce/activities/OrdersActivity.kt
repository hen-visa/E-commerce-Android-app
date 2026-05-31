package com.example.ecommerce.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapters.OrdersAdapter
import com.example.ecommerce.databinding.ActivityOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener { finish() }
        loadOrders()
    }

    private fun loadOrders() {
        val uid = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        db.collection("users").document(uid).collection("orders")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                binding.progressBar.visibility = View.GONE
                val orders = docs.documents.map { it.data ?: emptyMap() }
                if (orders.isEmpty()) {
                    binding.emptyTxt.visibility = View.VISIBLE
                } else {
                    binding.recyclerOrders.layoutManager = LinearLayoutManager(this)
                    binding.recyclerOrders.adapter = OrdersAdapter(orders)
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                binding.emptyTxt.visibility = View.VISIBLE
                binding.emptyTxt.text = "Failed to load orders. Please try again."
            }
    }
}