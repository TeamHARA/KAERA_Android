package com.hara.kaera.feature.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityLoginBinding
import com.hara.kaera.feature.MainActivity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.login.LoginViewModel.TokenState
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.kakao.sdk.auth.AuthApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val keyhash = Utility.getKeyHash(this)
//        Timber.e(keyhash.toString())
        binding.btnToken.onSingleClick {
            Timber.e(AuthApiClient.instance.hasToken().toString())
        }

        binding.btnLogout.onSingleClick {
            lifecycleScope.launch {
                kotlin.runCatching {
                    kaKaoLoginClient.logout()
                }.onSuccess {
                    // 데이터스토어 비우기
                    Timber.e("logout")
                    loginViewModel.kakaoLogOut()
                }.onFailure {
                    binding.root.makeToast("에러")
                    throw it
                }
            }

        }

        binding.btnKakaoLogin.onSingleClick(300) {
            lifecycleScope.launch {
                kotlin.runCatching {
                    kaKaoLoginClient.login()
                }.onSuccess {
                    // OAuthToken 뜯어서 서버에 리퀘스트바디로 전달
                    // DataStore에 저장
                    Timber.e(it.isSuccess.toString())
                    if (it.isSuccess) {
                        it.onSuccess { oAuthToken ->
                            Timber.e(oAuthToken.toString())
                            loginViewModel.getKakaoLoginJWT(oAuthToken = oAuthToken)
                        }
                    }
                }.onFailure {
                    // _oAuthTokenFlow.value = UiState.Error(ErrorType.Token)
                    //에러
                    binding.root.makeToast(it.toString())
                    throw it
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    loginViewModel.kakaoJWT.collect {
                        render(it)
                    }
                }
                launch {
                    loginViewModel.localJWT.collect {
                        tokenCheck(it)
                    }
                }
            }
        }
    }

    /*
    이 함수 자체가 카카오로그인을 한번 한 상태에서도 이루어지므로 나중에 splashActivity에서
    처리해도 되지 않을까 생각이 듭니다. 우선은 테스트용으로 여기에서 실행
     */
    private fun tokenCheck(tokenState: TokenState<String>) {
        when (tokenState) {
            is TokenState.Init -> Unit
            is TokenState.Empty -> {
                binding.root.makeToast("로그인 먼저 진행해주세요")
            }

            is TokenState.Valid -> {
                // 여기서는 나중에 토큰 재발급 동작(refresh 기반으로 accesstoken 재발급)이 있어야 할것
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun render(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> {}
            is UiState.Success -> {
                Timber.e(uiState.data)
                runBlocking {
                    loginViewModel.saveAccessToken(uiState.data)
                }
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }
        }
    }

}