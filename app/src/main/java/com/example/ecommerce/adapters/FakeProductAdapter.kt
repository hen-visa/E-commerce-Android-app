package com.example.ecommerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.model.api.FakeProduct

/**
 * Adapter for the REST-products RecyclerView (Requirement #3 — RecyclerView).
 * Uses [ListAdapter] + [DiffUtil] for efficient updates.
 */
class FakeProductAdapter(
    private val onItemClick: (FakeProduct) -> Unit = {}
) : ListAdapter<FakeProduct, FakeProductAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_fake_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView  = itemView.findViewById(R.id.imgProduct)
        private val tvTitle: TextView      = itemView.findViewById(R.id.tvProductTitle)
        private val tvPrice: TextView      = itemView.findViewById(R.id.tvProductPrice)
        private val tvCategory: TextView   = itemView.findViewById(R.id.tvProductCategory)
        private val ratingBar: RatingBar   = itemView.findViewById(R.id.ratingBarProduct)

        fun bind(item: FakeProduct) {
            tvTitle.text    = item.title
            tvPrice.text    = "$${String.format("%.2f", item.price)}"
            tvCategory.text = item.category.replaceFirstChar { it.uppercase() }
            ratingBar.rating = item.rating?.rate?.toFloat() ?: 0f

            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.grey_bg_10dp)
                .error(R.drawable.grey_bg_10dp)
                .into(imgProduct)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FakeProduct>() {
            override fun areItemsTheSame(old: FakeProduct, new: FakeProduct) = old.id == new.id
            override fun areContentsTheSame(old: FakeProduct, new: FakeProduct) = old == new
        }
    }
}
