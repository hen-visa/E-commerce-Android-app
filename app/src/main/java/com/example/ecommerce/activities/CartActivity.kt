package com.example.ecommerce.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.Helper.ChangeNumberItemsListener
import com.example.ecommerce.adapters.CartAdapter
import com.example.ecommerce.databinding.ActivityCartBinding
import com.example.ecommerce.helpers.ManagmentCart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        initView()
        calculateCart()
        initCartList()
    }

    private fun initCartList() {
        binding.apply {

            viewCart.layoutManager = LinearLayoutManager(
                this@CartActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            viewCart.adapter = CartAdapter(
                managmentCart.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )

            if (managmentCart.getListCart().isEmpty()) {
                emptyTxt.visibility = View.VISIBLE
                viewCart.visibility = View.GONE
            } else {
                emptyTxt.visibility = View.GONE
                viewCart.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.checkoutBtn.setOnClickListener {
            if (managmentCart.getListCart().isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, AddressActivity::class.java).apply {
                putExtra("orderTotal", calculateCartTotal())
            }

            startActivity(intent)
        }
    }

    private fun calculateCartTotal(): Double {
        val percentTax = 0.01
        val delivery = 10.0

        return Math.round(
            (managmentCart.getTotalFee() * (1 + percentTax) + delivery) * 100
        ) / 100.0
    }

    private fun calculateCart() {

        val percentTax = 0.01
        val delivery = 10.0

        tax = Math.round(
            managmentCart.getTotalFee() * percentTax * 100
        ) / 100.0

        val total = Math.round(
            (managmentCart.getTotalFee() + tax + delivery) * 100
        ) / 100.0

        val itemTotal = Math.round(
            managmentCart.getTotalFee() * 100
        ) / 100.0

        binding.apply {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }
}
