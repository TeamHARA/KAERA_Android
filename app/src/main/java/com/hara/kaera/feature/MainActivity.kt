package com.hara.kaera.feature

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMainBinding
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.DetailBeforeActivity
import com.hara.kaera.feature.dialog.DialogFullStoneFragment
import com.hara.kaera.feature.home.HomeFragment
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.storage.StorageFragment
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.navigateTo
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    private var time: Long = 0
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis()
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
        setPermission()
        this.onBackPressedDispatcher.addCallback(this, callback)
        registerBottomNav()
        checkIntent()
    }

    private fun setPermission() {

        launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val deniedPermissionList = permissions.filter { !it.value }.map { it.key }
                when {
                    deniedPermissionList.isNotEmpty() -> {
                        val map = deniedPermissionList.groupBy { permission ->
                            if (shouldShowRequestPermissionRationale(permission)) "denied" else "explained"
                        }
                        map["denied"]?.let {
                            // 최초거절 케이스 (앱 최초 설치이후 한번만 타게 됨)
                            binding.root.makeToast("원활한 서비스 이용을 위해서 알림권한을 허용해주세요!")
                        }
                        map["explained"]?.let {
                            //권한 영구 거절( 2번째 거절 이후 ) 메인에서는 아무런 동작도 안함
                        }
                    }

                    else -> {
                        val sharedPref: SharedPreferences =
                            applicationContext.getSharedPreferences(
                                com.hara.kaera.application.Constant.SHARED_PREFERENCE_NAME,
                                MODE_PRIVATE
                            )
                        if (!sharedPref.getBoolean(
                                com.hara.kaera.application.Constant.FCM_FIRST, false
                            )
                        ) {
                            binding.root.makeToast("마이페이지에서 알림을 활성화 해주세요!")
                        }
                    }
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            } else {
                launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }

        }
    }


    private fun registerBottomNav() {
        with(binding.bottomNav) {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        viewModel.setViewPagerPosition(0)
                        this@MainActivity.navigateTo<HomeFragment>(fragContainerId = R.id.cl_fragment_container)
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_write -> {
                        if (viewModel.isFullStone()) {
                            DialogFullStoneFragment().show(supportFragmentManager, "fullstone")
                        } else {
                            startActivity(
                                Intent(baseContext, WriteActivity::class.java).apply {
                                    putExtra("action", "write")
                                }
                            )
                        }
                        return@setOnItemSelectedListener false
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

    /*
    fcm payload 전달용
     */
    private fun checkIntent() {
        if (intent.hasExtra(Constant.worryIdIntent)) {
            val resultIntent = Intent(this@MainActivity, DetailBeforeActivity::class.java).apply {
                putExtra("action", "view")
                putExtra(
                    "worryDetail", WorryDetailEntity(
                        worryId = intent.getStringExtra(Constant.worryIdIntent)?.toInt() ?: -1,
                        title = "",
                        templateId = 0,
                        subtitles = emptyList(),
                        answers = emptyList(),
                        period = "",
                        updatedAt = "",
                        deadline = "",
                        dDay = -1,
                        finalAnswer = "",
                        review = WorryDetailEntity.Review(
                            content = "",
                            updatedAt = ""
                        )
                    )
                )
            }
            startActivity(resultIntent)
        }
    }
}