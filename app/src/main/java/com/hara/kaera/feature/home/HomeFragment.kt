package com.hara.kaera.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.home.gems.HomeFragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var adapter: HomeFragmentStateAdapter
    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.vpContainer.adapter = adapter

        setViewPager()
    }

    private fun setViewPager() {
        binding.vpDotsIndicator.attachTo(binding.vpContainer)
        binding.vpContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                homeViewModel.setViewPagerPosition(position)
                binding.tvLabelUnderLogo.text = when (position) {
                    0 -> getString(R.string.home_stone_label)
                    1 -> getString(R.string.home_jewel_label)
                    else -> ""
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        // TODO: [231009] WriteActivity -> DetailBeforeActivity -> 뒤로가기 눌러서 HomeFragment 돌아갈 때 때문에 일케 한 건데
        // TODO: 갱장히 찜찜하다.. 홈 원석화면 나올 때마다 render 되고, 서버 통신도 되니까..
        // TODO: 뭔가 fragmentStateAdapter 갱신하는 걸 잘 하면 될 거 같은데..
        adapter = HomeFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.vpContainer.adapter = adapter
        binding.vpContainer.currentItem = homeViewModel.viewPagerPosition.value
    }

    override fun onPause() {
        super.onPause()
        binding.vpContainer.adapter = null
    }

}