package com.example.mallandroid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mallandroid.R
import com.example.mallandroid.common.TokenManager
import com.example.mallandroid.common.GlobalTokenManager
import com.example.mallandroid.databinding.ActivityMainBinding
import com.example.mallandroid.ui.fragment.CartFragment
import com.example.mallandroid.ui.fragment.CategoryFragment
import com.example.mallandroid.ui.fragment.HomeFragment
import com.example.mallandroid.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalTokenManager.setToken(TokenManager.getToken(this))

        setupBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    switchFragment(HomeFragment())
                    true
                }
                R.id.nav_category -> {
                    switchFragment(CategoryFragment())
                    true
                }
                R.id.nav_cart -> {
                    switchFragment(CartFragment())
                    true
                }
                R.id.nav_profile -> {
                    switchFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        if (fragment != currentFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            currentFragment = fragment
        }
    }
}