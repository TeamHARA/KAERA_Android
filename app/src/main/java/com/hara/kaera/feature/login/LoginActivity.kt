package com.hara.kaera.feature.login

import androidx.activity.viewModels
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityLoginBinding
import com.hara.kaera.feature.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    val loginViewModel by viewModels<LoginViewModel>()

}