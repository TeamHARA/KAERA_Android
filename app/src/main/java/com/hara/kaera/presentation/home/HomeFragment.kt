package com.hara.kaera.presentation.home

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.presentation.home.model.Gem
import com.hara.kaera.presentation.base.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 서버 통신을 '두 탭에 대해' 다 한 다음에 화면 뿌려주기!

        binding.vpContainer.adapter = HomeAdapter().apply {
            // sample list
            submitList(
                listOf(
                    listOf( // 원석 list
                        Gem(R.drawable.gemstone_yellow, "이수현"),
                        Gem(R.drawable.gemstone_pink, "김준우"),
                        Gem(R.drawable.gemstone_red, "장유진"),
                        Gem(R.drawable.gemstone_pink, "4"),
                        Gem(R.drawable.gemstone_blue, "5"),
                        Gem(R.drawable.gemstone_pink, "6"),
                        Gem(R.drawable.gemstone_orange, "7"),
                        Gem(R.drawable.gemstone_pink, "8"),
                        Gem(R.drawable.gemstone_red, "9"),
                        Gem(R.drawable.gemstone_yellow, "10"),
                        Gem(R.drawable.gemstone_blue, "11"),
                        Gem(R.drawable.gemstone_green, "12")
                    ),
                    listOf( // 보석 list
                        Gem(R.drawable.gem_pink_l, "1"),
                        Gem(R.drawable.gem_green_l, "2"),
                        Gem(R.drawable.gem_blue_l, "3"),
                        Gem(R.drawable.gem_yellow_l, "4"),
                        Gem(R.drawable.gem_orange_l, "5"),
                        Gem(R.drawable.gem_red_l, "6"),
                        Gem(R.drawable.gem_pink_l, "7"),
                        Gem(R.drawable.gem_green_l, "8"),
                        Gem(R.drawable.gem_blue_l, "9"),
                        Gem(R.drawable.gem_red_l, "10"),
                        Gem(R.drawable.gem_orange_l, "11"),
                        Gem(R.drawable.gem_yellow_l, "12"),
                    ),
                )
            )
        }

        binding.vpDotsIndicator.attachTo(binding.vpContainer)

        binding.vpContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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