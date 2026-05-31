package com.example.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ViewholderSizeBinding

class SizeAdapter(
    private val items: ArrayList<String>,
    private val onSizeSelected: (String) -> Unit
) : RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    private var selectedPosition = -1

    class ViewHolder(val binding: ViewholderSizeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderSizeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = items[position]
        holder.binding.sizeText.text = size

        if (selectedPosition == position) {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.blue_bg)
            holder.binding.sizeText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.binding.sizeCard.cardElevation = 8f
        } else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.stroke_pink_bg)
            holder.binding.sizeText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.binding.sizeCard.cardElevation = 0f
        }

        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onSizeSelected(size)
        }
    }

    override fun getItemCount(): Int = items.size
}
