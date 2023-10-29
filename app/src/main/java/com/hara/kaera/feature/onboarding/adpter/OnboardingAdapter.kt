package com.hara.kaera.feature.onboarding.adpter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hara.kaera.feature.onboarding.OnboardingActivity
import com.hara.kaera.feature.onboarding.OnboardingFirstFragment
import com.hara.kaera.feature.onboarding.OnboardingSecondFragment
import com.hara.kaera.feature.onboarding.OnboardingThirdFragment

class OnboardingAdapter(onboardingActivity: OnboardingActivity) :
    FragmentStateAdapter(onboardingActivity) {
    private val fragmentList = listOf<Fragment>(
        OnboardingFirstFragment(),
        OnboardingSecondFragment(),
        OnboardingThirdFragment(),
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}
