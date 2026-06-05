package com.example.ecommerce.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.Helper.ChangeNumberItemsListener
import com.example.ecommerce.activities.AddressActivity
import com.example.ecommerce.adapters.CartAdapter
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.helpers.ManagmentCart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        managmentCart = ManagmentCart(requireContext())

        initView()
        refreshCart()
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun refreshCart() {
        calculateCart()
        initCartList()
        updateEmptyState()
    }

    private fun initCartList() {
        binding.viewCart.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.viewCart.adapter = CartAdapter(
            managmentCart.getListCart(),
            requireContext(),
            object : ChangeNumberItemsListener {
                override fun onChanged() {
                    refreshCart()
                }
            }
        )
    }

    private fun updateEmptyState() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.viewCart.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.viewCart.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        binding.checkoutBtn.setOnClickListener {

            if (managmentCart.getListCart().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Your cart is empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val intent = Intent(
                requireContext(),
                AddressActivity::class.java
            ).apply {
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

        binding.totalFeeTxt.text = "$$itemTotal"
        binding.taxTxt.text = "$$tax"
        binding.deliveryTxt.text = "$$delivery"
        binding.totalTxt.text = "$$total"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}