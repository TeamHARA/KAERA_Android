package com.hara.kaera.feature.home.gems

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeStoneBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.detail.DetailBeforeActivity
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.home.adapter.HomeStoneAdapter
import com.hara.kaera.feature.util.GridRvItemIntervalDecoration
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.dpToPx
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeStoneFragment : BindingFragment<FragmentHomeStoneBinding>(R.layout.fragment_home_stone) {
    private lateinit var homeStoneAdapter: HomeStoneAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("[ABC] HomeStoneFragment - onCreate()")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.e("[ABC] HomeStoneFragment - onViewStateRestored()")
    }

    override fun onResume() {
        super.onResume()
        Timber.e("[ABC] HomeStoneFragment - onResume()")
    }

    override fun onPause() {
        super.onPause()
        Timber.e("[ABC] HomeStoneFragment - onPause()")
    }

    override fun onStart() {
        super.onStart()

        viewModel.getHomeWorryList(false)
        Timber.e("[ABC] HomeStoneFragment - onStart()")
    }

    override fun onDestroy() {
        Timber.e("[ABC] HomeStoneFragment - onDestroy()")
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("[ABC] HomeStoneFragment - onViewCreated()")

        setRecyclerView()
        collectFlows()
    }

    private fun setRecyclerView() {
        homeStoneAdapter = HomeStoneAdapter(
            fun(worryId: Int) {
                startActivity(
                    Intent(context, DetailBeforeActivity::class.java).apply {
                        putExtra("worryId", worryId)
                    }
                )
            }
        )
        homeStoneAdapter.setHasStableIds(true)

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListStoneFlow.collect {
                    Timber.e("[ABC] [홈 화면/원석 뷰] 수집 ${it.toString()}") // TODO: [231009] 글쓰기에서 X누르면 왜 안 와?
                    render(it)
                }

            }
        }
    }

    private fun render(uiState: UiState<HomeWorryListEntity>) {
        when (uiState) {
            is UiState.Init -> Timber.e("[ABC] [홈 화면/원석 뷰] UiState.init")
            is UiState.Loading -> binding.loadingBar.visible(true)
            is UiState.Empty -> {
                showContent(true)
            }

            is UiState.Success<HomeWorryListEntity> -> {
                showContent(false)
                homeStoneAdapter.submitList(uiState.data.homeWorryList.toMutableList())
            }

            is UiState.Error -> {
                binding.loadingBar.visible(false)
                binding.root.makeToast(uiState.error)
            }
        }
    }

    private fun showContent(empty: Boolean) {
        binding.loadingBar.visible(false)
        binding.clEmpty.visible(empty)
        binding.rvHomeStones.visible(!empty)
    }

}