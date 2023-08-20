package com.hara.kaera.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private val worryList = arrayOfNulls<List<HomeWorryListEntity.HomeWorry>>(2) // [0] 원석 / [1] 보석
    private lateinit var adapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 서버 통신을 '두 탭에 대해' 다 한 다음에 화면 뿌려주기!
        adapter = HomeAdapter()
        binding.vpContainer.adapter = adapter

        /*
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
        */

        setViewPager()
        collectFlows()
        Timber.e("hi")
    }

    private fun setViewPager() {
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

    private fun collectFlows() {
        // 서버 통신 1) 원석 (해결 전 고민들)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListStoneFlow.collect {
                    render(0, it)
                }
            }
        }

        // 서버 통신 2) 원석 (해결 전 고민들)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListJewelFlow.collect {
                    render(1, it)
                }
            }
        }
    }

    private fun render(idx: Int, uiState: UiState<HomeWorryListEntity>) {
        when (uiState) {
            is UiState.Loading -> {
                Timber.e("loading...")
            }
            is UiState.Success -> {
                worryList[idx] = uiState.data.data
                if (worryList[0]?.isEmpty() == false && worryList[1]?.isEmpty() == false) {
                    adapter.submitList(worryList.toMutableList())
                }
            }
            is UiState.Error -> {
                Timber.e("error...")
            }
            else -> {
                Timber.e("else...")
            }
        }
    }
}