package com.hara.kaera.feature

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMainBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.home.HomeFragment
import com.hara.kaera.feature.storage.StorageFragment
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.PermissionRequestDelegator
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.navigateTo
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val homeFragment = HomeFragment()
    private val storageFragment = StorageFragment()

    private var time: Long = 0
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis()
                Timber.e("home backpress")
                KaeraSnackBar.make(
                    view = binding.root,
                    message = baseContext.stringOf(R.string.main_backpress),
                    duration = KaeraSnackBar.DURATION.SHORT,
                    backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                    locationY = Constant.completeSnackBarLocationY
                ).show()
            } else if (System.currentTimeMillis() - time < 2000) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PermissionRequestDelegator(this).checkPermissions(PermissionRequestDelegator.PLACE.HOME) == true) {
            binding.root.makeToast("원활한 서비스를 위해서 알림을 활성화 해 주세요!")
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
        //TODO 원석이 가득찰 경우 DialogFullStoneFragment().show(supportFragmentManager, "full_stone")
        //이거 수정하려면 HomeFragment에 있는 뷰모델을 액티비티에서 생성되도록 옮기고
        // 내부 아이템이 12면 바텀내비 부분에서 조건문으로 분기처리 해줘야 되겠네
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

    private fun isFragmentInBackStack(fragment: Fragment): Boolean {
        val backStackCount = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val entry = supportFragmentManager.getBackStackEntryAt(i)
            if (entry.name == fragment.javaClass.simpleName) {
                return true
            }
        }
        return false
    }
}