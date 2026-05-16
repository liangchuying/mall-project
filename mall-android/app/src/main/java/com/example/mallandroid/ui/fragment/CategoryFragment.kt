package com.example.mallandroid.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mallandroid.data.api.RetrofitClient
import com.example.mallandroid.data.model.Category
import com.example.mallandroid.databinding.FragmentCategoryBinding
import com.example.mallandroid.ui.adapter.LeftCategoryAdapter
import com.example.mallandroid.ui.adapter.RightCategoryAdapter
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var leftAdapter: LeftCategoryAdapter
    private lateinit var rightAdapter: RightCategoryAdapter
    private val categories = mutableListOf<Category>()
    private var selectedPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadCategories()
    }

    private fun setupRecyclerView() {
        leftAdapter = LeftCategoryAdapter(categories) { position ->
            selectedPosition = position
            leftAdapter.notifyDataSetChanged()
            if (categories.isNotEmpty()) {
                rightAdapter.updateData(categories[position].children ?: emptyList())
            }
        }

        rightAdapter = RightCategoryAdapter(emptyList())

        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leftAdapter
        }

        binding.subCategoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rightAdapter
        }
    }

    private fun loadCategories() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getCategoryTree()
                if (response.isSuccessful && response.body()?.isSuccess() == true) {
                    response.body()?.data?.let { categoryList ->
                        categories.clear()
                        categories.addAll(categoryList)
                        leftAdapter.notifyDataSetChanged()

                        if (categories.isNotEmpty()) {
                            rightAdapter.updateData(categories[0].children ?: emptyList())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}