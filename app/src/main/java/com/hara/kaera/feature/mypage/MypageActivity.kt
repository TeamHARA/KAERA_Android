package com.hara.kaera.feature.mypage

import android.content.Intent
import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.mypage.custom.DialogMypage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
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
                    // TODO: 로그아웃 로직
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
