package com.hara.kaera.feature.mypage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.login.LoginActivity
import com.hara.kaera.feature.mypage.custom.DialogMypage
import com.hara.kaera.feature.util.KaKaoLoginClient
import com.hara.kaera.feature.util.PermissionRequestDelegator
import com.hara.kaera.feature.util.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {

    private val myPageViewModel by viewModels<MypageViewModel>()
    private lateinit var permissionRequestDelegator :  PermissionRequestDelegator
    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequestDelegator = PermissionRequestDelegator(this)
        setClickListener()
        grantPermission()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    myPageViewModel.savedName.collect {
                        binding.vm = myPageViewModel
                    }
                }
                launch {
                    myPageViewModel.permissionGranted.collect{
                        binding.tbAlertToggle.isChecked = it
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(!myPageViewModel.permissionGranted.value)
            revokeSelfPermissionOnKill(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    private fun grantPermission(){
        myPageViewModel.permissionChanged(
            ContextCompat.checkSelfPermission(baseContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )

        binding.tbAlertToggle.setOnClickListener {
            if(myPageViewModel.permissionGranted.value){
                Timber.e("ture_check")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    myPageViewModel.permissionChanged(
                        ContextCompat.checkSelfPermission(baseContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    )
                }
            }else{
                Timber.e("false_check")
                permissionRequestDelegator.checkPermissions()
            }
        }
        // 현재 상황 권한 삭제가 실시간으로 반영이 되지 않고 앱 재시작시 반영 그러므로
        // onDestoy에서 체크박스가 체크되어있으면 revoke를 부르는 방식으로 하는것
    }

    private fun setClickListener() {
        with(binding) {
            clService.setOnClickListener {
                // TODO: notion 링크 변경
                startActivity(
                    Intent(this@MypageActivity, WebViewActivity::class.java).apply {
                        putExtra("url", "https://www.naver.com/")
                    },
                )
            }
            clPrivacy.setOnClickListener {
                // TODO: notion 링크 변경
                startActivity(
                    Intent(this@MypageActivity, WebViewActivity::class.java).apply {
                        putExtra("url", "https://github.com/TeamHARA/KAERA_Android")
                    },
                )
            }
            tvLogout.setOnClickListener {
                DialogMypage("logout") {
                    lifecycleScope.launch {
                        kotlin.runCatching {
                            kaKaoLoginClient.logout()
                        }.onSuccess {
                            // 데이터스토어 비우기
                            Timber.e("logout")
                            myPageViewModel.clearDataStore()
                            startActivity(Intent(baseContext, LoginActivity::class.java))
                            finishAffinity()
                        }.onFailure {
                            binding.root.makeToast("잠시후 다시 시도해주세요.")
                            throw it
                        }
                    }
                }.show(supportFragmentManager, "logout")
            }
            tvSignout.setOnClickListener {
                DialogMypage("signout") {
                    lifecycleScope.launch {
                        kotlin.runCatching {
                            kaKaoLoginClient.unLink()
                        }.onSuccess {
                            // 데이터스토어 비우기
                            Timber.e("logout")
                            myPageViewModel.clearDataStore()
                            startActivity(Intent(baseContext, LoginActivity::class.java))
                            finishAffinity()
                        }.onFailure {
                            binding.root.makeToast("잠시후 다시 시도해주세요.")
                            throw it
                        }
                    }
                }.show(supportFragmentManager, "signout")
            }
        }
    }
}
