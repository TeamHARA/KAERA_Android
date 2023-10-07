package com.hara.kaera.feature.mypage

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.login.LoginActivity
import com.hara.kaera.feature.mypage.custom.DialogMypage
import com.hara.kaera.feature.util.KaKaoLoginClient
import com.hara.kaera.feature.util.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {

    private val myPageViewModel by viewModels<MypageViewModel>()

    @Inject
    lateinit var kaKaoLoginClient: KaKaoLoginClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            myPageViewModel.savedName.collect {
                binding.vm = myPageViewModel
            }
        }
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
                            myPageViewModel.kakaoLogOut()
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
                    // TODO: 탈퇴 로직
                }.show(supportFragmentManager, "signout")
            }
        }
    }
}
