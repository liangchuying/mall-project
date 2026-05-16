package com.example.mallandroid.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mallandroid.R
import com.example.mallandroid.data.model.Category
import androidx.core.graphics.toColorInt

class LeftCategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<LeftCategoryAdapter.LeftViewHolder>() {

    private var selectedPosition = 0

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_left, parent, false)
        return LeftViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeftViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.name

        if (position == selectedPosition) {
            holder.tvCategoryName.setBackgroundColor(Color.WHITE)
            holder.tvCategoryName.setTextColor("#FF6200EE".toColorInt())
        } else {
            holder.tvCategoryName.setBackgroundColor("#FFF5F5F5".toColorInt())
            holder.tvCategoryName.setTextColor("#FF333333".toColorInt())
        }

        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            val newPosition = holder.bindingAdapterPosition
            if (newPosition != RecyclerView.NO_POSITION) {
                selectedPosition = newPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(newPosition)
                onItemClick(newPosition)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }
}