package com.hara.kaera.feature.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityOnboardingBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.login.LoginActivity
import com.hara.kaera.feature.onboarding.adpter.OnboardingAdapter

class OnboardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewPager()
        setButtonClickListener()
    }

    private fun setViewPager() {
        onboardingAdapter = OnboardingAdapter(this)
        binding.vpContainer.adapter = onboardingAdapter
        binding.vpDotsIndicator.attachTo(binding.vpContainer)
        binding.viewPager = binding.vpContainer

        // 페이지 변화 감지
        binding.vpContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.invalidateAll() // 바인딩을 다시 평가
            }
        })
    }

    private fun setButtonClickListener() {
        binding.btnNext.setOnClickListener {
            val currentItem = binding.vpContainer.currentItem
            if (currentItem < onboardingAdapter.itemCount - 1) { // 현재 페이지가 마지막 페이지가 아니면 다음 페이지로 이동
                binding.vpContainer.setCurrentItem(currentItem + 1, true)
            } else { // 마지막 페이지인 경우 액티비티 종료 및 새 액티비티 시작
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
