package com.example.ecommerce.fragments

import android.os.Bundle
import android.view.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wishlist = WishlistManager(requireContext()).getList()
        if (wishlist.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
        } else {
            val adapter = PopularAdapter(wishlist)
            binding.recyclerWishlist.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerWishlist.adapter = adapter
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}