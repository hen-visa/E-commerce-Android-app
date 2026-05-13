package com.example.ecommerce.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.model.BrandModel

import android.view.ViewGroup
import android.content.res.ColorStateList
import android.view.LayoutInflater
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ViewholderBrandBinding


class BrandsAdapter(private val items: MutableList<BrandModel>):
    RecyclerView.Adapter<BrandsAdapter.Viewholder>() {
    private var selectionPosition = -1
    private var lastSelectionPosition = -1

    fun updateData(newData:List<BrandModel>){
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()

    }
    class Viewholder (val binding: ViewholderBrandBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrandsAdapter.Viewholder {
        val binding = ViewholderBrandBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BrandsAdapter.Viewholder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.pic)

        holder.binding.root.setOnClickListener {
            lastSelectionPosition = selectionPosition
            selectionPosition = position
            if(lastSelectionPosition != -1) notifyItemChanged(lastSelectionPosition)
            notifyItemChanged(selectionPosition)
        }
        val isSelected = selectionPosition == position

        holder.binding.pic.setBackgroundResource(
            if(isSelected){
                R.drawable.blue_bg
            }else{
                R.drawable.grey_bg
            }
        )

        ImageViewCompat.setImageTintList(
            holder.binding.pic,
            ColorStateList.valueOf(
                holder.itemView.context.getColor(
                    if (isSelected) R.color.white else R.color.black
                )
            )
        )

    }


    override fun getItemCount(): Int = items.size
}