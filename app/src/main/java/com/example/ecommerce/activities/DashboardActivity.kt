package com.example.ecommerce.activities


import android.media.Image
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ecommerce.adapters.BrandsAdapter
import com.example.ecommerce.adapters.PopularAdapter
import com.example.ecommerce.adapters.SliderAdapter
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.model.SliderModel
import com.example.ecommerce.viewModel.MainViewModel

class DashboardActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: ActivityMainBinding

    private val brandsAdapter = BrandsAdapter(mutableListOf())

    private val popularAdapter= PopularAdapter(mutableListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initBrands()
        initBanners()
        initPopular()

    }

    private fun initPopular() {
        binding.apply {
            recyclerViewPopular.layoutManager= LinearLayoutManager(this@DashboardActivity)
            recyclerViewPopular.adapter = popularAdapter
            progressBarPopular.visibility = View.VISIBLE

            viewModel.popular.observe(this@DashboardActivity){data->
                popularAdapter.updateDate(data)
                progressBarPopular.visibility = View.GONE
            }
            viewModel.loadPopular()
        }
    }


    private fun initBrands() {
        binding.apply {
            recyclerViewBrands.layoutManager =
                LinearLayoutManager(
                    this@DashboardActivity,
                    LinearLayoutManager.HORIZONTAL, false
                )
            recyclerViewBrands.adapter = brandsAdapter
            progressBarBrands.visibility = View.VISIBLE

            viewModel.brands.observe(this@DashboardActivity) { data ->
                brandsAdapter.updateData(data)
                progressBarBrands.visibility = View.GONE
            }
            viewModel.loadBrands()

        }
    }


    private fun initBanners() {
        binding.apply {
            progressBarBanner.visibility = View.VISIBLE
            viewModel.banners.observe(this@DashboardActivity) { data ->
                setupBanners(data)
                progressBarBanner.visibility = View.GONE
            }
            viewModel.loadBanners()
        }
    }


    private fun setupBanners(image: List<SliderModel>) {

        binding.viewpagerSlider.apply {

            adapter = SliderAdapter(image, this)

            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            (getChildAt(0) as? RecyclerView)?.overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer(
                CompositePageTransformer().apply {

                    addTransformer(MarginPageTransformer(40))

                })
        }

        binding.dotIndicator.apply {
            visibility=if(image.size>1) View.VISIBLE else View.GONE
            if (image.size>1) attachTo(binding.viewpagerSlider)
        }

    }
}