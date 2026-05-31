package com.example.ecommerce.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.Helper.ChangeNumberItemsListener
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ViewholderCartBinding
import com.example.ecommerce.helpers.ManagmentCart
import com.example.ecommerce.model.ItemsModel
import java.util.Locale

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {

    private val managmentCart = ManagmentCart(context)

    class Viewholder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = listItemSelected[position]
        holder.binding.apply {
            titleTxt.text = item.title
            feeEachItemTxt.text = "$${String.format(Locale.US, "%.2f", item.price)}"
            
            val totalPrice = item.numberInCart * item.price
            totalEachItem.text = "$${String.format(Locale.US, "%.2f", totalPrice)}"
            
            numberItemTxt.text = item.numberInCart.toString()

            Glide.with(holder.itemView.context)
                .load(item.picUrl.firstOrNull())
                .placeholder(R.drawable.grey_bg_10dp)
                .error(R.drawable.grey_bg_10dp)
                .into(pic)

            plusCartBtn.setOnClickListener {
                managmentCart.plusItem(listItemSelected, holder.bindingAdapterPosition, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }

            minusCartBtn.setOnClickListener {
                managmentCart.minusItem(listItemSelected, holder.bindingAdapterPosition, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
