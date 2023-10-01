package com.hara.kaera.feature

import android.content.Intent
import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMainBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.home.HomeFragment
import com.hara.kaera.feature.storage.StorageFragment
import com.hara.kaera.feature.util.PermissionRequestDelegator
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.navigateTo
import com.hara.kaera.feature.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PermissionRequestDelegator(this).checkPermissions() == true) {
            binding.root.makeToast("원활한 서비스를 위해서 알림을 활성화 해주세요!")
        }
        registerBottomNav()
    }

    private fun registerBottomNav() {
        with(binding.bottomNav) {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        this@MainActivity.navigateTo<HomeFragment>(fragContainerId = R.id.cl_fragment_container)
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_write -> {
                        startActivity(Intent(context, WriteActivity::class.java))
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_storage -> {
                        this@MainActivity.navigateTo<StorageFragment>(fragContainerId = R.id.cl_fragment_container)
                        return@setOnItemSelectedListener true
                    }

                    else -> return@setOnItemSelectedListener false
                }
            }
            itemIconTintList = null
            itemActiveIndicatorColor = null
            selectedItemId = R.id.nav_home
        }
    }
}