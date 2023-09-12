package com.hara.kaera.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeStoneBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.home.adapter.HomeStoneAdapter
import com.hara.kaera.presentation.util.GridRvItemIntervalDecoration
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeStoneFragment : BindingFragment<FragmentHomeStoneBinding>(R.layout.fragment_home_stone) {
    private lateinit var homeStoneAdapter: HomeStoneAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        collectFlows()
    }

    private fun setRecyclerView() {
        homeStoneAdapter = HomeStoneAdapter()
        binding.rvHomeStones.run {
            adapter = homeStoneAdapter
            addItemDecoration(
                GridRvItemIntervalDecoration(
                    spanCount = 3,
                    verticalMargin = 18.dpToPx(requireContext()),
                    horizontalMargin = 26
                )
            )
        }
    }

    // 서버 통신 1) 원석 (해결 전 고민들)
    private fun collectFlows() {
//        homeStoneAdapter.submitList(
//            listOf( // 원석 list
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0),
//                HomeWorryListEntity.HomeWorry(0, 1, "PLEASE", 0)
//            )
//        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListStoneFlow.collect {
                    render(it)
                }
            }
        }
    }

    private fun render(uiState: UiState<HomeWorryListEntity>) {
        when (uiState) {
            is UiState.Init -> Timber.e("[홈 화면/원석 뷰] UiState.init")
            is UiState.Loading -> Timber.e("[홈 화면/원석 뷰] UiState.Loading")
            is UiState.Empty -> {
                binding.clEmpty.visibility = View.VISIBLE
                binding.rvHomeStones.visibility = View.GONE
            }
            is UiState.Success<HomeWorryListEntity> -> {
                binding.clEmpty.visibility = View.GONE
                binding.rvHomeStones.visibility = View.VISIBLE
                homeStoneAdapter.submitList(uiState.data.homeWorryList)
            }
            is UiState.Error -> Timber.e("[홈 화면/원석 뷰] UiState.Error")
        }
    }
}