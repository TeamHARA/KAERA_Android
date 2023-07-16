package com.hara.kaera.presentation

import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMainBinding
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.navigateTo

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerBottomNav()
    }

    private fun registerBottomNav() {
        with (binding.bottomNav) {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        this@MainActivity.navigateTo<HomeFragment>(fragContainerId = R.id.cl_fragment_container)
                        return@setOnItemSelectedListener true
                    }
                    R.id.nav_write -> {
                        // activity로 이동
                        return@setOnItemSelectedListener true
                    }
                    R.id.nav_storage -> {
                        this@MainActivity.navigateTo<TempStorageFragment>(fragContainerId = R.id.cl_fragment_container)
                        return@setOnItemSelectedListener true
                    }
                    else -> return@setOnItemSelectedListener false
                }
            }
            itemIconTintList = null
            backgroundTintList = null
            selectedItemId = R.id.nav_home
        }
    }
}