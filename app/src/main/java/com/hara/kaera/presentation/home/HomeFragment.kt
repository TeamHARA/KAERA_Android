package com.hara.kaera.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.presentation.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    //private val worryList = arrayOfNulls<List<HomeWorryListEntity.HomeWorry>>(2) // [0] 원석 / [1] 보석
    private lateinit var adapter: HomeFragmentStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 서버 통신을 '두 탭에 대해' 다 한 다음에 화면 뿌려주기!
        adapter = HomeFragmentStateAdapter(requireActivity())
        binding.vpContainer.adapter = adapter

        setViewPager()
    }

    private fun setViewPager() {
        binding.vpDotsIndicator.attachTo(binding.vpContainer)
        binding.vpContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvLabelUnderLogo.text = when (position) {
                    0 -> getString(R.string.home_stone_label)
                    1 -> getString(R.string.home_jewel_label)
                    else -> ""
                }
            }
        })
    }
}