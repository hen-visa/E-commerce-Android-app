package com.example.ecommerce.adapters

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class OrdersAdapter(
    private val orders: List<Map<String, Any>>
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSummary: TextView = view.findViewById(R.id.tvOrderSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        val total = order["total"] ?: "N/A"
        val addr  = order["address"] ?: ""
        holder.tvSummary.text = "Order #${position + 1} — $$total\n$addr"
    }

    override fun getItemCount() = orders.size
}