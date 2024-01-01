package com.hara.kaera.feature.home.gems

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeJewelBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.detail.DetailAfterActivity
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.home.adapter.HomeJewelAdapter
import com.hara.kaera.feature.util.GridRvItemIntervalDecoration
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.dpToPx
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeJewelFragment : BindingFragment<FragmentHomeJewelBinding>(R.layout.fragment_home_jewel) {
    private lateinit var homeJewelAdapter: HomeJewelAdapter
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onStart() {
        super.onStart()
        viewModel.getHomeWorryList(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setClickListeners()
        collectFlows()
    }

    private fun setRecyclerView() {
        homeJewelAdapter = HomeJewelAdapter(
            fun(worryId: Int) {
                startActivity(
                    Intent(context, DetailAfterActivity::class.java).apply {
                        putExtra("worryId", worryId)
                    }
                )
            }
        )
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

    private fun setClickListeners() {
        with(binding.layoutError) {
            layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getHomeWorryList(true)
            }
            layoutInternalError.btnInternalError.onSingleClick {
                viewModel.getHomeWorryList(true)
            }
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
            is UiState.Init -> Unit
            is UiState.Loading -> binding.loadingBar.visible(true)
            is UiState.Empty -> controlLayout(success = true, empty = true)

            is UiState.Success<HomeWorryListEntity> -> {
                controlLayout(success = true)
                homeJewelAdapter.submitList(uiState.data.homeWorryList)
            }

            is UiState.Error -> {
                controlLayout(success = false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    root = binding.root
                )
            }
        }
    }

    private fun controlLayout(success: Boolean, empty: Boolean = false) {
        binding.loadingBar.visible(false)
        binding.rvHomeJewels.visible(success && !empty)
        binding.clEmpty.visible(success && empty)
        binding.layoutError.root.visible(!success)
    }
}