package com.example.ecommerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

/**
 * Simple adapter that renders FakeStore category strings as chip-style rows.
 * Supports a click callback for filtering products by category.
 */
class ApiCategoryAdapter(
    private val categories: MutableList<String> = mutableListOf(),
    private val onCategoryClick: (String) -> Unit = {}
) : RecyclerView.Adapter<ApiCategoryAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_ID.toInt()

    fun updateData(newCategories: List<String>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_api_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategory.text = category.replaceFirstChar { it.uppercase() }
        holder.tvCategory.isSelected = (position == selectedPosition)
        holder.itemView.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = position
            if (prev != RecyclerView.NO_ID.toInt()) notifyItemChanged(prev)
            notifyItemChanged(position)
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategoryName)
    }
}
