package com.hara.kaera.feature.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hara.kaera.BuildConfig
import com.hara.kaera.R
import com.hara.kaera.application.FirebaseMessagingService
import com.hara.kaera.databinding.ActivitySplashBinding
import com.hara.kaera.feature.MainActivity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.dialog.DialogRestartFragment
import com.hara.kaera.feature.onboarding.OnboardingActivity
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.KaKaoLoginClient
import com.hara.kaera.feature.util.TokenState
import com.hara.kaera.feature.util.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    val startViewModel by viewModels<StartViewModel>()

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIntent()
        checkNetwork()
    }

    /*
    fcm payload 전달용
     */
    private fun checkIntent(): Intent {

        return if (intent.hasExtra(Constant.worryIdIntent)) {
            Intent(this@StartActivity, MainActivity::class.java).apply {
                putExtra(Constant.worryIdIntent, intent.getStringExtra(Constant.worryIdIntent))
            }
        } else {
            Intent(this@StartActivity, MainActivity::class.java)
        }
    }

    private fun checkNetwork() {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (networkInfo?.isConnected != true) {
            DialogRestartFragment(
                clickListener = { checkNetwork() }
            ).show(supportFragmentManager, "restart")
        } else {
            checkAppVersion()
        }
    }

    private fun splashAnimated() {
        startViewModel.getSavedRefreshToken()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.flowLogo.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(800)
                    .withEndAction {
                        // 첫 번째 애니메이션 종료 시 실행할 코드
                        // 두 번째 애니메이션 시작
                        binding.tvSlogan.apply {
                            alpha = 0f
                            visibility = View.VISIBLE
                            animate()
                                .alpha(1f)
                                .setDuration(400)
                                .setStartDelay(800).withEndAction {
                                    // 두 번째 애니메이션 종료 시 실행할 코드
                                    collectState()
                                }
                        }
                    }
            }
        }, 800)
    }

    private fun tokenCheck(tokenState: TokenState<String>) {
        when (tokenState) {
            is TokenState.Init -> Unit
            is TokenState.Empty -> {
                // 데이터스토어에 토큰이 비어있는 상태
                // 카카오로그인을 위해서 로그인 액티비티로 이동
                startActivity(Intent(this, OnboardingActivity::class.java))
                finishAffinity()
            }

            is TokenState.Exist -> Unit
            // 이 상태 진입은 미리 이전 토큰이 저장되어있는 상태이므로
            // 토큰 재발급 호출 후 그 결과에 따라 데이터 스토어에 액세스토큰 갱신
            // 이때 만료상태라면 최초로그인 과정으로 진입


            is TokenState.Valid -> {
                startActivity(checkIntent())
                finishAffinity()
            }

            is TokenState.Expired -> {
                // 저장된 리프레시 토큰이 만료된 상태
                // 이전에 카카오로그인은 했다는 의미이므로
                // 카카오 로그인을 다시 거쳐서 JWT (리프레시/액세스) 모두 갱신해야 한다
                kakaoLogin()
            }

            is TokenState.Error -> {
                binding.root.makeToast("잠시후 다시 시도해주세요.")
            }
        }
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    startViewModel.tokenState.collect {
                        tokenCheck(it)
                    }
                }
            }
        }
    }

    private fun kakaoLogin() { // 최초 혹은 이후 카카오로그인 과정
        lifecycleScope.launch {
            kotlin.runCatching {
                kaKaoLoginClient.login()
            }.onSuccess {
                // OAuthToken 뜯어서 서버에 리퀘스트바디로 전달
                // DataStore에 저장
                runBlocking {
                    startViewModel.setDeviceToken(
                        FirebaseMessagingService().getDeviceToken(
                            baseContext
                        ) ?: "Null"
                    )
                }
                if (it.isSuccess) {
                    it.onSuccess { oAuthToken ->
                        Timber.e(oAuthToken.toString())
                        startViewModel.callKakaoLoginJWT(oAuthToken = oAuthToken)
                    }
                }
            }.onFailure {
                //에러
                throw it
            }
        }
    }

    /*
        config를 통해서 앱 업데이트 확인
     */
    private fun checkAppVersion() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Timber.e(remoteConfig.getDouble(Constant.CONFIG_APP_VERSION).toString())
                Timber.e(remoteConfig.getBoolean(Constant.CONFIG_FORCE_UPDATE).toString())
                showUpdateDialog(
                    remoteConfig.getDouble(Constant.CONFIG_APP_VERSION).toInt(),
                    remoteConfig.getBoolean(Constant.CONFIG_FORCE_UPDATE)
                )
            } else {
                Timber.e(it.toString())
            }
        }
    }

    /*
        현재 버전이 앱버전보다 낮을 경우 업데이트 , 강제업데이트의 경우 재설치 이전까지 무한루프
        권자 업데이트의 경우 아니오 누르면 이후 스플래시 로직 이어지도록
     */
    private fun showUpdateDialog(appVersion: Int, forceUpdate: Boolean) {
        val currentVersion = BuildConfig.VERSION_CODE
        if (appVersion > currentVersion) {
            if (!forceUpdate) {
                DialogUpdateFragment(this.getString(R.string.dialog_app_essential_update), {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details")))
                }, { checkAppVersion() }).show(supportFragmentManager, "update")
            } else {
                DialogUpdateFragment(this.getString(R.string.dialog_app_felxible_update), {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details")))
                }, { splashAnimated() }
                ).show(supportFragmentManager, "update")
            }
        } else {
            splashAnimated()
        }
    }

}