package com.hara.kaera.feature.home.gems

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// 이 adapter를 통해 ViewPager2에 Fragment들을 연결시켜 노출시킨다

class HomeFragmentStateAdapter(
    fm: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    private var homeStoneFragment: HomeStoneFragment = HomeStoneFragment()
    private var homeJewelFragment: HomeJewelFragment = HomeJewelFragment()

    // 1. ViewPager2에 연결할 Fragment 들을 생성
    private var fragmentList = arrayListOf(homeStoneFragment, homeJewelFragment)

    // 2. ViewPager2에서 노출시킬 Fragment 의 갯수 설정
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // 3. ViewPager2의 각 페이지에서 노출할 Fragment 설정
    override fun createFragment(position: Int): Fragment {
//        Timber.e("[ABC] HomeFragmentStateAdapter - createFragment(${position})")
        return when (position) {
            0 -> homeStoneFragment
            else -> homeJewelFragment
        }
    }

}