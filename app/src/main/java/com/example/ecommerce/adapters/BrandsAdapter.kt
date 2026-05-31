package com.example.ecommerce.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.activities.CategoryActivity
import com.example.ecommerce.databinding.ViewholderBrandBinding
import com.example.ecommerce.model.BrandModel

class BrandsAdapter(private val items: MutableList<BrandModel>) :
    RecyclerView.Adapter<BrandsAdapter.Viewholder>() {
    private var selectionPosition = -1
    private var lastSelectionPosition = -1

    fun updateData(newData: List<BrandModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class Viewholder(val binding: ViewholderBrandBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderBrandBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        val isSelected = selectionPosition == position

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .placeholder(R.drawable.grey_bg_10dp)
            .error(R.drawable.grey_bg_10dp)
            .into(holder.binding.pic)

        holder.binding.root.setOnClickListener {
            lastSelectionPosition = selectionPosition
            selectionPosition = position
            if (lastSelectionPosition != -1) notifyItemChanged(lastSelectionPosition)
            notifyItemChanged(selectionPosition)

            // Open CategoryActivity
            val ctx = holder.itemView.context
            val intent = Intent(ctx, CategoryActivity::class.java).apply {
                putExtra("categoryId", item.id)
                putExtra("categoryTitle", item.title)
            }
            ctx.startActivity(intent)
        }

        holder.binding.pic.setBackgroundResource(
            if (isSelected) {
                R.drawable.blue_bg
            } else {
                R.drawable.grey_bg_10dp
            }
        )

        val tintColor = if (isSelected) R.color.white else R.color.black
        ImageViewCompat.setImageTintList(
            holder.binding.pic,
            ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, tintColor)
            )
        )
    }

    override fun getItemCount(): Int = items.size
}
