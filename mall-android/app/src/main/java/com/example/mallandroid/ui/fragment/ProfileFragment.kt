package com.example.mallandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mallandroid.common.TokenManager
import com.example.mallandroid.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        updateUserInfo()
    }

    private fun setupListeners() {
        binding.userHeader.setOnClickListener {
            if (TokenManager.isLoggedIn(requireContext())) {
                // TODO: 跳转到个人信息页面
            } else {
                // TODO: 跳转到登录页面
            }
        }

        binding.tvMyOrders.setOnClickListener {
            if (TokenManager.isLoggedIn(requireContext())) {
                // TODO: 跳转到订单列表页面
            } else {
                // TODO: 跳转到登录页面
            }
        }

        binding.tvCoupons.setOnClickListener {
            if (TokenManager.isLoggedIn(requireContext())) {
                // TODO: 跳转到优惠券页面
            } else {
                // TODO: 跳转到登录页面
            }
        }

        binding.tvAddresses.setOnClickListener {
            if (TokenManager.isLoggedIn(requireContext())) {
                // TODO: 跳转到收货地址页面
            } else {
                // TODO: 跳转到登录页面
            }
        }

        binding.btnLogout.setOnClickListener {
            TokenManager.clearToken(requireContext())
            TokenManager.clearUserInfo(requireContext())
            updateUserInfo()
        }
    }

    private fun updateUserInfo() {
        if (TokenManager.isLoggedIn(requireContext())) {
            binding.tvUsername.text = TokenManager.getUsername(requireContext()) ?: "用户"
            binding.tvUserInfo.visibility = View.GONE
            binding.btnLogout.visibility = View.VISIBLE
        } else {
            binding.tvUsername.text = "点击登录"
            binding.tvUserInfo.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}