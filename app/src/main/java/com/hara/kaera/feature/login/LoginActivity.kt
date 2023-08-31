package com.hara.kaera.feature.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityLoginBinding
import com.hara.kaera.feature.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            kotlin.runCatching {
                kaKaoLoginClient.login()
            }.onSuccess {
                // OAuthToken 뜯어서 서버에 리퀘스트바디로 전달
                // DataStore에 저장
            }
                .onFailure {
                    // _oAuthTokenFlow.value = UiState.Error(ErrorType.Token)
                    //에러
                }
        }

    }

}