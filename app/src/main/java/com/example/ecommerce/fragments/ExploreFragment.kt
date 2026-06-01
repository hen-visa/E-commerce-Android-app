package com.example.ecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapters.ApiCategoryAdapter
import com.example.ecommerce.adapters.FakeProductAdapter
import com.example.ecommerce.api.NetworkResult
import com.example.ecommerce.databinding.FragmentExploreBinding
import com.example.ecommerce.viewModel.FakeStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ExploreFragment"

/**
 * ExploreFragment — NEW screen (satisfies Requirement #1 ≥4 screens).
 *
 * Demonstrates all three REST API endpoints:
 *   • GET /products          → productAdapter  (RecyclerView)
 *   • GET /products/categories → categoryAdapter (RecyclerView)
 *   • GET /users             → user count shown in subtitle
 *
 * Lifecycle methods are logged per Requirement #2 (Part 2).
 */
@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FakeStoreViewModel by viewModels()

    private val productAdapter  = FakeProductAdapter()
    private val categoryAdapter = ApiCategoryAdapter()

    // ── Lifecycle Requirement — Part 2 ─────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        setupRecyclerViews()
        observeViewModels()
        viewModel.loadAll()          // triggers all 3 endpoints
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart — ExploreFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume — ExploreFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause — ExploreFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop — ExploreFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Lifecycle", "onDestroyView — ExploreFragment")
        _binding = null
    }

    // ── Setup ───────────────────────────────────────────────────────────────

    private fun setupRecyclerViews() {
        binding.rvApiProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
        binding.rvApiCategories.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = categoryAdapter
        }

        // Category chip click → filter products
        categoryAdapter.apply {
            // re-create with the click lambda
        }
    }

    // ── Observe ─────────────────────────────────────────────────────────────

    private fun observeViewModels() {

        // Endpoint 1 — Products
        viewModel.products.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.progressProducts.visibility = View.VISIBLE
                    binding.rvApiProducts.visibility    = View.GONE
                }
                is NetworkResult.Success -> {
                    binding.progressProducts.visibility = View.GONE
                    binding.rvApiProducts.visibility    = View.VISIBLE
                    productAdapter.submitList(result.data)
                    Log.d(TAG, "Products loaded: ${result.data.size}")
                }
                is NetworkResult.Error -> {
                    binding.progressProducts.visibility = View.GONE
                    showError("Products error: ${result.message}")
                    Log.e(TAG, "Products error: ${result.message}")
                }
            }
        }

        // Endpoint 2 — Categories
        viewModel.categories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.progressCategories.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.progressCategories.visibility = View.GONE
                    categoryAdapter.updateData(result.data)
                    Log.d(TAG, "Categories loaded: ${result.data}")
                }
                is NetworkResult.Error -> {
                    binding.progressCategories.visibility = View.GONE
                    showError("Categories error: ${result.message}")
                }
            }
        }

        // Endpoint 3 — Users
        viewModel.users.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.tvUserCount.text = "Loading users…"
                }
                is NetworkResult.Success -> {
                    binding.tvUserCount.text = "${result.data.size} registered users"
                    Log.d(TAG, "Users loaded: ${result.data.size}")
                }
                is NetworkResult.Error -> {
                    binding.tvUserCount.text = "Could not load users"
                    Log.e(TAG, "Users error: ${result.message}")
                }
            }
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
}
