package com.example.ecommerce.fragments

import android.os.Bundle
import android.view.*
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapters.PopularAdapter
import com.example.ecommerce.databinding.FragmentWishlistBinding
import com.example.ecommerce.helpers.WishlistManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWishlist()
    }

    override fun onResume() {
        super.onResume()
        loadWishlist()
    }

    private fun loadWishlist() {
        val wishlist = WishlistManager(requireContext()).getList()

        if (wishlist.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.recyclerWishlist.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.recyclerWishlist.visibility = View.VISIBLE

            binding.recyclerWishlist.layoutManager =
                LinearLayoutManager(requireContext())

            binding.recyclerWishlist.adapter =
                PopularAdapter(wishlist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}