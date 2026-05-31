package com.example.ecommerce.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.databinding.ActivityConfirmOrderBinding
import com.example.ecommerce.helpers.ManagmentCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmOrderBinding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val orderTotal = intent.getDoubleExtra("orderTotal", 0.0)

        binding.tvName.text = "Name: $name"
        binding.tvAddress.text = "Address: $address"
        binding.tvTotal.text = "Total: $${"%.2f".format(orderTotal)}"

        binding.btnConfirm.setOnClickListener {
            placeOrder(name, address, orderTotal)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun placeOrder(name: String, address: String, total: Double) {

        val uid = auth.currentUser?.uid

        if (uid == null) {
            Toast.makeText(
                this,
                "Please log in to place an order",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnConfirm.isEnabled = false

        val cart = ManagmentCart(this).getListCart()

        if (cart.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.btnConfirm.isEnabled = true

            Toast.makeText(
                this,
                "Cart is empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val realtimeDbRef = FirebaseDatabase
            .getInstance()
            .getReference("Items")

        var checkedItems = 0
        var hasError = false

        cart.forEach { cartItem ->

            realtimeDbRef.orderByChild("title")
                .equalTo(cartItem.title)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (hasError) return

                        if (!snapshot.exists()) {

                            hasError = true

                            binding.progressBar.visibility = View.GONE
                            binding.btnConfirm.isEnabled = true

                            Toast.makeText(
                                this@ConfirmOrderActivity,
                                "${cartItem.title} not found",
                                Toast.LENGTH_LONG
                            ).show()

                            return
                        }

                        snapshot.children.forEach { child ->

                            val currentStock =
                                child.child("stockCount")
                                    .getValue(Int::class.java) ?: 0

                            // OUT OF STOCK
                            if (currentStock == 0) {

                                hasError = true

                                binding.progressBar.visibility = View.GONE
                                binding.btnConfirm.isEnabled = true

                                Toast.makeText(
                                    this@ConfirmOrderActivity,
                                    "${cartItem.title} is Out of Stock",
                                    Toast.LENGTH_LONG
                                ).show()

                                return
                            }

                            // LOW STOCK WARNING
                            if (currentStock <= 5) {

                                Toast.makeText(
                                    this@ConfirmOrderActivity,
                                    "Only $currentStock left for ${cartItem.title}!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // NOT ENOUGH STOCK
                            if (cartItem.numberInCart > currentStock) {

                                hasError = true

                                binding.progressBar.visibility = View.GONE
                                binding.btnConfirm.isEnabled = true

                                Toast.makeText(
                                    this@ConfirmOrderActivity,
                                    "Not enough stock for ${cartItem.title}",
                                    Toast.LENGTH_LONG
                                ).show()

                                return
                            }
                        }

                        checkedItems++

                        // ALL ITEMS CHECKED
                        if (checkedItems == cart.size && !hasError) {

                            val orderData = hashMapOf(
                                "name" to name,
                                "address" to address,
                                "total" to total,
                                "timestamp" to FieldValue.serverTimestamp(),
                                "items" to cart.map {
                                    mapOf(
                                        "title" to it.title,
                                        "qty" to it.numberInCart,
                                        "price" to it.price
                                    )
                                }
                            )

                            db.collection("users")
                                .document(uid)
                                .collection("orders")
                                .add(orderData)
                                .addOnSuccessListener {

                                    // DECREASE STOCK
                                    cart.forEach { item ->

                                        realtimeDbRef.orderByChild("title")
                                            .equalTo(item.title)
                                            .addListenerForSingleValueEvent(
                                                object : ValueEventListener {

                                                    override fun onDataChange(snapshot: DataSnapshot) {

                                                        snapshot.children.forEach { child ->

                                                            val current =
                                                                child.child("stockCount")
                                                                    .getValue(Int::class.java)
                                                                    ?: 0

                                                            val updatedStock =
                                                                maxOf(
                                                                    0,
                                                                    current - item.numberInCart
                                                                )

                                                            child.ref.child("stockCount")
                                                                .setValue(updatedStock)
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                    }
                                                })
                                    }

                                    // CLEAR CART
                                    ManagmentCart(this@ConfirmOrderActivity)
                                        .clearCart()

                                    binding.progressBar.visibility = View.GONE

                                    Toast.makeText(
                                        this@ConfirmOrderActivity,
                                        "Order placed successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val intent = Intent(
                                        this@ConfirmOrderActivity,
                                        DashboardActivity::class.java
                                    )

                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                                Intent.FLAG_ACTIVITY_NEW_TASK

                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->

                                    binding.progressBar.visibility = View.GONE
                                    binding.btnConfirm.isEnabled = true

                                    Toast.makeText(
                                        this@ConfirmOrderActivity,
                                        "Order failed: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                        hasError = true

                        binding.progressBar.visibility = View.GONE
                        binding.btnConfirm.isEnabled = true

                        Toast.makeText(
                            this@ConfirmOrderActivity,
                            error.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}