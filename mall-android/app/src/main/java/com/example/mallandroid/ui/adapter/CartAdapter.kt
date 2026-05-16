package com.example.mallandroid.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mallandroid.R
import com.example.mallandroid.data.model.CartItem

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onItemCheckedChangeListener: (CartItem, Boolean) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbSelected: CheckBox = itemView.findViewById(R.id.cbSelected)
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvMinus: TextView = itemView.findViewById(R.id.tvMinus)
        val tvPlus: TextView = itemView.findViewById(R.id.tvPlus)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.cbSelected.isChecked = item.selected
        holder.tvProductName.text = item.productName
        holder.tvPrice.text = "¥${item.price}"
        holder.tvQuantity.text = item.quantity.toString()

        item.productImage?.let { imageUrl ->
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.ivProductImage)
        }

        holder.cbSelected.setOnCheckedChangeListener { _, isChecked ->
            item.selected = isChecked
            onItemCheckedChangeListener(item, isChecked)
        }

        holder.tvMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                holder.tvQuantity.text = item.quantity.toString()
                updateTotalPrice()
            }
        }

        holder.tvPlus.setOnClickListener {
            item.quantity++
            holder.tvQuantity.text = item.quantity.toString()
            updateTotalPrice()
        }

        holder.ivDelete.setOnClickListener {
            // TODO: 删除购物车商品
        }
    }

    private fun updateTotalPrice() {
        onItemCheckedChangeListener(cartItems.firstOrNull() ?: return, true)
    }

    override fun getItemCount(): Int = cartItems.size
}