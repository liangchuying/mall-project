package com.example.mallandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mallandroid.R
import com.example.mallandroid.data.model.Category

class RightCategoryAdapter(private var categories: List<Category>) :
    RecyclerView.Adapter<RightCategoryAdapter.RightViewHolder>() {

    class RightViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: android.widget.TextView =
            itemView.findViewById(R.id.tvCategoryName)
        val subCategoryRecyclerView: RecyclerView =
            itemView.findViewById(R.id.subCategoryRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_right, parent, false)
        return RightViewHolder(view)
    }

    override fun onBindViewHolder(holder: RightViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.name

        val subCategories = category.children ?: emptyList()
        if (subCategories.isNotEmpty()) {
            holder.subCategoryRecyclerView.apply {
                layoutManager = GridLayoutManager(holder.itemView.context, 3)
                adapter = SubCategoryAdapter(subCategories)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

class SubCategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

    class SubCategoryViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_category, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.itemView.findViewById<android.widget.TextView>(R.id.tvCategoryName).text =
            category.name
    }

    override fun getItemCount(): Int = categories.size
}