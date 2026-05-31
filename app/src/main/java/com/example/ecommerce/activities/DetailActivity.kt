package com.example.ecommerce.activities

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.adapters.ColorAdapter
import com.example.ecommerce.adapters.SizeAdapter
import com.example.ecommerce.databinding.ActivityDetailBinding
import com.example.ecommerce.helpers.ManagmentCart
import com.example.ecommerce.helpers.WishlistManager
import com.example.ecommerce.model.ItemsModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart
    private lateinit var wishlistManager: WishlistManager
    
    private var selectedSize: String? = null
    private var selectedColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        wishlistManager = WishlistManager(this)

        val receivedItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("object", ItemsModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("object") as? ItemsModel
        }

        if (receivedItem != null) {
            item = receivedItem
            item.numberInCart = 1
            setupView()
            setupSizeList()
            setupColorList()
        } else {
            Toast.makeText(this, "Product data is missing", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupColorList() {
        binding.colorList.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ColorAdapter(item.color) { color ->
                selectedColor = color
            }
        }
    }

    private fun setupSizeList() {
        binding.sizeList.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = SizeAdapter(item.size) { size ->
                selectedSize = size
            }
        }
    }

    private fun setupView() = with(binding) {
        titleTxt.text = item.title
        descriptionTxt.text = item.description
        priceTxt.text = "$${item.price}"
        numberItemTxt.text = item.numberInCart.toString()

        updateTotalPrice()
        updateFavIcon()
        displayStockStatus()

        Glide.with(this@DetailActivity)
            .load(item.picUrl.firstOrNull())
            .placeholder(R.drawable.grey_bg)
            .into(picMain)

        backBtn.setOnClickListener { finish() }

        favBtn.setOnClickListener {
            wishlistManager.toggle(item)
            updateFavIcon()
        }

        plusBtn.setOnClickListener {
            if (item.numberInCart < item.stockCount) {
                item.numberInCart++
                numberItemTxt.text = item.numberInCart.toString()
                updateTotalPrice()
            } else {
                Toast.makeText(this@DetailActivity, "Maximum stock reached", Toast.LENGTH_SHORT).show()
            }
        }

        minusBtn.setOnClickListener {
            if (item.numberInCart > 1) {
                item.numberInCart--
                numberItemTxt.text = item.numberInCart.toString()
                updateTotalPrice()
            }
        }

        addToCartBtn.setOnClickListener {
            if (selectedSize == null && item.size.isNotEmpty()) {
                Toast.makeText(this@DetailActivity, "Please select a size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedColor == null && item.color.isNotEmpty()) {
                Toast.makeText(this@DetailActivity, "Please select a color", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (item.stockCount > 0) {
                managmentCart.insert(item)
                Toast.makeText(this@DetailActivity, "Added to Cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayStockStatus() = with(binding) {
        when {
            item.stockCount == 0 -> {
                stockTxt.text = "Out of Stock"
                stockTxt.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.red))
                addToCartBtn.isEnabled = false
                addToCartBtn.alpha = 0.5f
                addToCartBtn.text = "Unavailable"
            }
            item.stockCount <= 10 -> {
                stockTxt.text = "Only ${item.stockCount} items left!"
                stockTxt.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.orange))
            }
            else -> {
                stockTxt.text = "In Stock: ${item.stockCount} items"
                stockTxt.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.green))
            }
        }
    }

    private fun updateFavIcon() {
        val isFav = wishlistManager.isFavorite(item)

        binding.favBtn.setImageResource(
            if (isFav)
                R.drawable.favorite
            else
                R.drawable.fav_icon
        )
    }

    private fun updateTotalPrice() = with(binding) {
        val total = item.price * item.numberInCart
        totalPriceTxt.text = "$${String.format("%.2f", total)}"
    }
}
