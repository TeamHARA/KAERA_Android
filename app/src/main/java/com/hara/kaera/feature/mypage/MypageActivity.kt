package com.hara.kaera.feature.mypage

import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityMypageBinding
import com.hara.kaera.feature.base.BindingActivity

class MypageActivity : BindingActivity<ActivityMypageBinding>(R.layout.activity_mypage) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setClickListener()
    }
    private fun setClickListener() {
        with(binding) {
            clService.setOnClickListener {
                // notion 이동
            }
            clPrivacy.setOnClickListener {
                // notion 이동
            }
            tvLogout.setOnClickListener {
                // 알럿
            }
            tvSignout.setOnClickListener {
                // 알럿
            }
        }
    }
}