package com.example.ecommerce.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        binding.btnContinue.setOnClickListener {
            val name    = binding.etFullName.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val city    = binding.etCity.text.toString().trim()
            val phone   = binding.etPhone.text.toString().trim()

            if (name.isEmpty() || address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmOrderActivity::class.java).apply {
                putExtra("name", name)
                putExtra("address", "$address, $city")
                putExtra("phone", phone)
                putExtra("orderTotal", this@AddressActivity.intent.getDoubleExtra("orderTotal", 0.0))
            }
            startActivity(intent)
        }
    }
}
