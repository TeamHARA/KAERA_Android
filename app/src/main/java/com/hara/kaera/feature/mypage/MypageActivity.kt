package com.hara.kaera.feature.mypage

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.hara.kaera.R
import com.hara.kaera.application.Constant
import com.hara.kaera.application.FirebaseMessagingService
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.mypage.custom.DialogMypage
import com.hara.kaera.feature.onboarding.OnboardingActivity
import com.hara.kaera.feature.util.KaKaoLoginClient
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {

    private val myPageViewModel by viewModels<MypageViewModel>()

    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    private val sharedPref: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    }

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
        setNotification()
        collectFlow()
    }

    override fun onResume() {
        super.onResume()
        // 시스템 설정창에서 권한 허용하고 들어온 경우 / 다이얼로그를 통해서 허용한 경우 뷰 갱신
        notificationView()
    }

    private fun setNotification() {
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
                            //권한 영구 거절( 2번째 거절 이후 ) 이때부터는 사용자가 직접 시스템 설정창에서 권한을 주어야 하므로 시스템 설정창으로 이동
                            this.startActivity(
                                Intent().setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                    .putExtra(
                                        Settings.EXTRA_APP_PACKAGE, this.baseContext.packageName
                                    )
                            )
                        }
                    }

                    else -> {
                        //권한이 허가 되었을때
                        notificationView()
                    }
                }
            }

        with(binding.tbAlertToggle) {
            this.isChecked = sharedPref.getBoolean(Constant.FCM_ACTIVATE_KEY, false)
            onSingleClick(1000) {
                // 토글 온 오프는 서버에서의 FCM 활성화 유무에 달림
                if (!this.isChecked) {
                    // FCM 비활성화
                    callPushAlarmActivated(0)
                } else {
                    // FCM 활성화
                    callPushAlarmActivated(1)
                }
            }
        }
        binding.tvAllow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    //TODO rationale dialog
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
    }

    private fun setClickListener() {
        with(binding) {
            with(btnClose) {
                increaseTouchSize(baseContext)
                setOnClickListener {
                    finish()
                }
            }
            layoutKaeraGuide.root.setOnClickListener {
                connectWebView(WebViewConstant.kaeraGuide)
            }
            layoutKaeraInstagram.root.setOnClickListener {
                connectWebView(WebViewConstant.instagram)
            }
            layoutOpenSource.root.setOnClickListener {
                OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스 목록")
                startActivity(Intent(this@MypageActivity, OssLicensesMenuActivity::class.java))
            }
            layoutService.root.setOnClickListener {
                connectWebView(WebViewConstant.useOfTerms)
            }
            layoutPrivacy.root.setOnClickListener {
                connectWebView(WebViewConstant.privacyTerms)
            }

            btnLogout.setOnClickListener {
                DialogMypage("logout") {
                    myPageViewModel.serviceLogout()
                    callPushAlarmActivated(0)
                }.show(supportFragmentManager, "logout")
            }

            tvSignout.setOnClickListener {
                DialogMypage("unregister") {
                    myPageViewModel.serviceUnRegister()
                    callPushAlarmActivated(0)
                }.show(supportFragmentManager, "unregister")
            }

        }
    }

    private fun notificationView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            binding.tvAllow.visible(!granted)
            binding.tbAlertToggle.visible(granted)
        }
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
                    myPageViewModel.uiStateFlow.collect {
                        when (it) {
                            is UiState.Loading -> binding.clLoading.visible(true)

                            is UiState.Success -> {
                                binding.clLoading.visible(false)
                                when (it.data) {
                                    is SignOutType.LOGOUT -> {
                                        kaKaoLoginClient.logout()
                                        binding.root.makeToast("로그아웃이 완료되었습니다.")
                                    }

                                    is SignOutType.UNLINK -> {
                                        kaKaoLoginClient.unLink()
                                        binding.root.makeToast("회원탈퇴가 완료되었습니다.")
                                    }

                                    is SignOutType.CLEAR -> {
                                        startActivity(
                                            Intent(
                                                baseContext,
                                                OnboardingActivity::class.java
                                            )
                                        )
                                        finishAffinity()
                                    }

                                }
                            }

                            is UiState.Error -> {
                                binding.clLoading.visible(false)
                                binding.root.makeToast(it.error)
                            }

                            else -> Unit
                        }

                    }
                }
                launch {
                    myPageViewModel.pushState.collect {
                        when (it) {
                            is UiState.Loading -> binding.clLoading.visible(true)

                            is UiState.Success -> {
                                binding.clLoading.visible(false)
                                KaeraSnackBar.make(
                                    view = binding.root,
                                    message = it.data,
                                    duration = KaeraSnackBar.DURATION.SHORT,
                                    backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                                    locationY = 0.0
                                ).show()
                                binding.tbAlertToggle.isChecked = it.data == "알림 활성화 성공"
                                if (it.data != "알림 활성화 성공") sharedPref.edit().putBoolean(
                                    Constant.FCM_ACTIVATE_KEY,
                                    false
                                ).apply() else {
                                    sharedPref.edit()
                                        .putBoolean(Constant.FCM_ACTIVATE_KEY, true).apply()
                                    sharedPref.edit().putBoolean(Constant.FCM_FIRST, true).apply()
                                }
                            }

                            is UiState.Error -> {
                                binding.tbAlertToggle.isChecked = !(binding.tbAlertToggle.isChecked)
                                binding.clLoading.visible(false)
                                binding.root.makeToast(it.error)
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun callPushAlarmActivated(isTrued: Int) {
        myPageViewModel.pushAlarmActivated(
            isTrued,
            FirebaseMessagingService().getDeviceToken(baseContext) ?: "null"
        )
    }

    private fun connectWebView(targetUrl: String) {
        startActivity(
            Intent(
                this@MypageActivity, WebViewActivity::class.java
            ).apply {
                putExtra(
                    WebViewConstant.urlIntent,
                    targetUrl
                )
            }
        )
    }

    object WebViewConstant {
        const val urlIntent = "url"
        const val kaeraGuide =
            "https://daffy-lawyer-1b8.notion.site/Kaera-bd1d79798c2542728761fa628e49ada6?pvs=4"
        const val useOfTerms =
            "https://daffy-lawyer-1b8.notion.site/e4383e48fd2a4e32b44d9d01ba663fd5"
        const val privacyTerms =
            "https://daffy-lawyer-1b8.notion.site/baf26a6459024af89fdfec26031adcf1"
        const val instagram =
            "https://www.instagram.com/kaera.app/?igshid=OGQ5ZDc2ODk2ZA%3D%3D&utm_source=qr"
    }

    sealed class SignOutType<Unit> {
        object LOGOUT : SignOutType<Unit>()
        object UNLINK : SignOutType<Unit>()
        object CLEAR : SignOutType<Unit>()
    }

}
