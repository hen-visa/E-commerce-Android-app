package com.example.ecommerce.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ViewholderColorBinding

class ColorAdapter(
    private val items: ArrayList<String>,
    private val onColorSelected: (String) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    private var selectedPosition = -1

    class ViewHolder(val binding: ViewholderColorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderColorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorHex = items[position]
        
        try {
            holder.binding.colorCircle.setColorFilter(Color.parseColor(colorHex))
        } catch (e: Exception) {
            holder.binding.colorCircle.setColorFilter(Color.GRAY)
        }

        if (selectedPosition == position) {
            holder.binding.strokeView.visibility = View.VISIBLE
        } else {
            holder.binding.strokeView.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onColorSelected(colorHex)
        }
    }

    override fun getItemCount(): Int = items.size
}
