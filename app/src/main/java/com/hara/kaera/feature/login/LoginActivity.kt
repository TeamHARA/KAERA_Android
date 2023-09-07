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
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyhash = Utility.getKeyHash(this)
        Timber.e(keyhash.toString())

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
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.kakaoJWT.collect {
                    render(it)
                }
            }
        }

    }

    private fun render(uistate: UiState<String>) {
        when (uistate) {
            is UiState.Init -> Unit
            is UiState.Loading -> {}
            is UiState.Success -> {
                Timber.e(uistate.data)
                //finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }

            is UiState.Error -> {
                binding.root.makeToast(uistate.error)
            }
        }


    }

}