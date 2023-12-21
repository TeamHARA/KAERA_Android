package com.hara.kaera.feature.mypage

import android.Manifest
import android.content.Intent
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
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.login.LoginActivity
import com.hara.kaera.feature.mypage.custom.DialogMypage
import com.hara.kaera.feature.onboarding.OnboardingActivity
import com.hara.kaera.feature.util.KaKaoLoginClient
import com.hara.kaera.feature.util.PermissionRequestDelegator
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {

    private val myPageViewModel by viewModels<MypageViewModel>()

    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
        setNotification()
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
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 시스템 설정창에서 권한 허용하고 들어온 경우 / 다이얼로그를 통해서 허용한 경우 뷰 갱신
        notificationView()
    }

    private fun grantPermission() {

                    else -> {
                        //권한이 허가 되었을때
                        notificationView()
                    }
                }
            }

        binding.tbAlertToggle.onSingleClick {
            // 토글 온 오프는 서버에서의 FCM 활성화 유무에 달림
            if (false) {
                // 여기서 서버에 fcm 토큰을 삭제하고 체크하고 등록하는 로직이 필요함!
                // 안드로이드에서 즉시 프로그래밍적으로 알림권한을 삭제하거나 비활성화 시키는 방법은 없음(앱의 재시작이 필요)
                // 따라서 알림권한이 잇는가 -> 그다음 fcm 토큰이서버에 등록되어 있는가? 둘다 true이면 토클이 isChecked true
                // 하나라도 false일 경우 isCheceked가 false로 될 필요가 있다 따라서.
                // 서버측에 fcm 관련 api가 필요하다
                // FCM 활성화
            } else {
                // FCM 비활성화
            }
        }
        binding.tvAllow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    Timber.e("ration")

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
                //TODO 릴리즈 버전에서 라이선스 확인하기
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
                    lifecycleScope.launch {
                        kotlin.runCatching {
                            myPageViewModel.serviceLogout()
                        }.onSuccess {
                            // 알림 비활성화
                            Timber.e("logout")
                            kaKaoLoginClient.logout()
                            binding.root.makeToast("로그아웃이 완료되었습니다.")
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
                DialogMypage("unregister") {
                    lifecycleScope.launch {
                        kotlin.runCatching {
                            myPageViewModel.serviceUnRegister()
                        }.onSuccess {
                            // 알림 비활성화
                            Timber.e("unlink")
                            kaKaoLoginClient.unLink()
                            binding.root.makeToast("회원탈퇴가 완료되었습니다.")
                            startActivity(Intent(baseContext, OnboardingActivity::class.java))
                            finishAffinity()
                        }.onFailure {
                            binding.root.makeToast("잠시후 다시 시도해주세요.")
                            throw it
                        }
                    }
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

}
