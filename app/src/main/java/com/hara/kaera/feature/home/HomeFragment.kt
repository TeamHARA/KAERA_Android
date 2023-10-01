package com.hara.kaera.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.home.gems.HomeFragmentStateAdapter
import com.hara.kaera.feature.home.gems.HomeJewelFragment
import com.hara.kaera.feature.home.gems.HomeStoneFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: HomeFragmentStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("[ABC] HomeFragment - OnViewCreated()")

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
        Timber.e("[ABC] HomeFragment - OnResume()")

        // TODO: [231009] WriteActivity -> DetailBeforeActivity -> 뒤로가기 눌러서 HomeFragment 돌아갈 때 때문에 일케 한 건데
        // TODO: 갱장히 찜찜하다.. 홈 원석화면 나올 때마다 render 되고, 서버 통신도 되니까..
        // TODO: 뭔가 fragmentStateAdapter 갱신하는 걸 잘 하면 될 거 같은데..
        adapter = HomeFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.vpContainer.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        Timber.e("[ABC] HomeFragment - OnPause()")
        binding.vpContainer.adapter = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.e("[ABC] HomeFragment - OnDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("[ABC] HomeFragment - OnDestroy()")
    }
}