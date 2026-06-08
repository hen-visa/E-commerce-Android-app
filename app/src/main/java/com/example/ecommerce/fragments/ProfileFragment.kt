package com.example.ecommerce.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.activities.AuthActivity
import com.example.ecommerce.activities.OrdersActivity
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser

        binding.tvName.text = user?.displayName ?: "Guest"
        binding.tvEmail.text = user?.email ?: ""

        Glide.with(this)
            .load(user?.photoUrl)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .circleCrop()
            .into(binding.imgAvatar)

        binding.btnOrders.setOnClickListener {
            startActivity(
                Intent(requireContext(), OrdersActivity::class.java)
            )
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()

            startActivity(
                Intent(requireContext(), AuthActivity::class.java)
            )

            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}