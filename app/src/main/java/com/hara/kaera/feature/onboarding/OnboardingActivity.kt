package com.hara.kaera.feature.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityOnboardingBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.login.LoginActivity
import com.hara.kaera.feature.onboarding.adpter.OnboardingAdapter
import com.hara.kaera.feature.util.stringOf

class OnboardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private lateinit var onboardingAdapter: OnboardingAdapter

    private var time: Long = 0
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis()
                KaeraSnackBar.make(
                    view = binding.root,
                    message = baseContext.stringOf(R.string.main_backpress),
                    duration = KaeraSnackBar.DURATION.SHORT,
                    backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                    locationY = -300.0
                ).show()
            } else if (System.currentTimeMillis() - time < 2000) {
                finishAffinity()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)
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
