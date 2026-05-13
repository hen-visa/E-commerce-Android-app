package com.example.ecommerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.model.SliderModel
import android.content.Context
import android.widget.ImageView


class SliderAdapter (
    private var sliderItems: List<SliderModel>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)


        fun setImage(sliderItem: SliderModel,context: Context) {

            Glide.with(context)
                .load(sliderItem.url)
                .into(imageView)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderAdapter.SliderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderAdapter.SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position],holder.itemView.context)
          if (position==sliderItems.lastIndex-1){
              viewPager2.post (runnable)

          }
    }

    override fun getItemCount(): Int = sliderItems.size
    private val runnable = Runnable{
        sliderItems = sliderItems
        notifyDataSetChanged()
    }
}