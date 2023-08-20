package com.hara.kaera.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeJewelBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.home.adapter.HomeJewelAdapter
import com.hara.kaera.presentation.util.GridRvItemIntervalDecoration
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.dpToPx
import com.hara.kaera.presentation.util.pxToDp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeJewelFragment : BindingFragment<FragmentHomeJewelBinding>(R.layout.fragment_home_jewel) {
    private lateinit var homeJewelAdapter: HomeJewelAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        collectFlows()
    }

    private fun setRecyclerView() {
        homeJewelAdapter = HomeJewelAdapter()
        binding.rvHomeJewels.run {
            adapter = homeJewelAdapter
            addItemDecoration(
                GridRvItemIntervalDecoration(
                    spanCount = 3,
                    verticalMargin = 19.dpToPx(requireContext()),
                    horizontalMargin = 26
                )
            )
        }
    }

    // 서버 통신 2) 원석 (해결 전 고민들)
    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListJewelFlow.collect {
                    render(it)
                }
            }
        }
    }

    private fun render(uiState: UiState<HomeWorryListEntity>) {
        when (uiState) {
            is UiState.Loading -> {
                Timber.e("loading...")
            }

            is UiState.Success -> {
                if (uiState.data.data?.isEmpty() == false) {
                    homeJewelAdapter.submitList(uiState.data.data)
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