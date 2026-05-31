package com.example.ecommerce.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ecommerce.adapters.BrandsAdapter
import com.example.ecommerce.adapters.PopularAdapter
import com.example.ecommerce.adapters.SliderAdapter
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.model.SliderModel
import com.example.ecommerce.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val brandsAdapter = BrandsAdapter(mutableListOf())
    private val popularAdapter = PopularAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBrands()
        initBanners()
        initPopular()
        initSearch()

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initSearch() {
        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.filteredPopular.observe(viewLifecycleOwner) { data ->
            // Fix: Corrected method name from updateDate to updateData
            popularAdapter.updateData(data.toMutableList())
            binding.emptyPopularTxt.visibility =
                if (data.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun initPopular() {
        binding.apply {
            recyclerViewPopular.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewPopular.adapter = popularAdapter
            progressBarPopular.visibility = View.VISIBLE

            viewModel.popular.observe(viewLifecycleOwner) {
                progressBarPopular.visibility = View.GONE
            }
            viewModel.loadPopular()
        }
    }

    private fun initBrands() {
        binding.apply {
            recyclerViewBrands.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewBrands.adapter = brandsAdapter
            progressBarBrands.visibility = View.VISIBLE

            viewModel.brands.observe(viewLifecycleOwner) { data ->
                brandsAdapter.updateData(data)
                progressBarBrands.visibility = View.GONE
            }
            viewModel.loadBrands()
        }
    }

    private fun initBanners() {
        binding.apply {
            progressBarBanner.visibility = View.VISIBLE
            viewModel.banners.observe(viewLifecycleOwner) { data ->
                setupBanners(data)
                progressBarBanner.visibility = View.GONE
            }
            viewModel.loadBanners()
        }
    }

    private fun setupBanners(images: List<SliderModel>) {
        binding.viewpagerSlider.apply {
            adapter = SliderAdapter(images, this)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            (getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
            })
        }
        binding.dotIndicator.apply {
            visibility = if (images.size > 1) View.VISIBLE else View.GONE
            if (images.size > 1) attachTo(binding.viewpagerSlider)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
