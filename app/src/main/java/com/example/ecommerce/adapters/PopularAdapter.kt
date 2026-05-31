package com.example.ecommerce.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.activities.DetailActivity
import com.example.ecommerce.databinding.ViewholderPopularBinding
import com.example.ecommerce.model.ItemsModel

class PopularAdapter(
    private val items: MutableList<ItemsModel>
): RecyclerView.Adapter<PopularAdapter.Viewholder>() {

    fun updateData(newData: List<ItemsModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderPopularBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            titleTxt.text = item.title
            priceTxt.text = "$${item.price}"
            ratingTxt.text = item.rating.toString()

            // Fix: Use root.context, the correct image URL from item, and target the correct ImageView (pic)
            Glide.with(root.context)
                .load(item.picUrl.firstOrNull())
                .placeholder(R.drawable.grey_bg_10dp)
                .error(R.drawable.grey_bg_10dp)
                .into(pic)

            root.setOnClickListener {
                val intent = Intent(root.context, DetailActivity::class.java)
                intent.putExtra("object", item)
                root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class Viewholder(val binding: ViewholderPopularBinding):
        RecyclerView.ViewHolder(binding.root)
}
