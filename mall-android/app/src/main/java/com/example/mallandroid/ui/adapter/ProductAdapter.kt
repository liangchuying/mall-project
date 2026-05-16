package com.example.mallandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mallandroid.R
import com.example.mallandroid.data.model.Product

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage = itemView.findViewById<android.widget.ImageView>(R.id.ivProductImage)
        val tvProductName = itemView.findViewById<TextView>(R.id.tvProductName)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvSales = itemView.findViewById<TextView>(R.id.tvSales)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductName.text = product.name
        holder.tvPrice.text = "¥${product.price}"
        holder.tvSales.text = "已售 ${product.sales}"

        product.image?.let { imageUrl ->
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.ivProductImage)
        }
    }

    override fun getItemCount(): Int = products.size
}