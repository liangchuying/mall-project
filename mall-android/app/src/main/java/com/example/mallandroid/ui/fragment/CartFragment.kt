package com.example.mallandroid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mallandroid.data.api.RetrofitClient
import com.example.mallandroid.data.model.CartItem
import com.example.mallandroid.databinding.FragmentCartBinding
import com.example.mallandroid.ui.adapter.CartAdapter
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private var totalPrice = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        loadCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { item, selected ->
            updateTotalPrice()
        }
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupListeners() {
        binding.cbSelectAll.setOnClickListener {
            val allSelected = (it as CheckBox).isChecked
            cartItems.forEach { it.selected = allSelected }
            cartAdapter.notifyDataSetChanged()
            updateTotalPrice()
        }

        binding.btnCheckout.setOnClickListener {
            // TODO: 跳转到订单确认页面
        }
    }

    private fun updateTotalPrice() {
        totalPrice = cartItems.filter { it.selected }.sumOf { it.price * it.quantity }
        binding.tvTotalPrice.text = "¥${String.format("%.2f", totalPrice)}"
        val selectedCount = cartItems.count { it.selected }
        binding.btnCheckout.text = "去结算($selectedCount)"
    }

    private fun loadCart() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getCartList()
                if (response.isSuccessful && response.body()?.isSuccess() == true) {
                    response.body()?.data?.let { items ->
                        cartItems.clear()
                        cartItems.addAll(items)
                        cartAdapter.notifyDataSetChanged()

                        if (items.isEmpty()) {
                            binding.emptyView.visibility = View.VISIBLE
                            binding.bottomBar.visibility = View.GONE
                        } else {
                            binding.emptyView.visibility = View.GONE
                            binding.bottomBar.visibility = View.VISIBLE
                            updateTotalPrice()
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