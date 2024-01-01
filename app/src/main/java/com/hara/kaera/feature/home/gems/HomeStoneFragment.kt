package com.hara.kaera.feature.home.gems

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeStoneBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.detail.DetailBeforeActivity
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.home.adapter.HomeStoneAdapter
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.GridRvItemIntervalDecoration
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.dpToPx
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeStoneFragment : BindingFragment<FragmentHomeStoneBinding>(R.layout.fragment_home_stone) {
    private lateinit var homeStoneAdapter: HomeStoneAdapter
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getHomeWorryList(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setClickListeners()
        collectFlows()
    }

    private fun setRecyclerView() {
        homeStoneAdapter = HomeStoneAdapter(
            fun(worryId: Int) {
                startActivity(
                    Intent(context, DetailBeforeActivity::class.java).apply {
                        putExtra("action", "view")
                        putExtra(
                            "worryDetail", WorryDetailEntity(
                                worryId = worryId,
                                title = "",
                                templateId = 0,
                                subtitles = emptyList(),
                                answers = emptyList(),
                                period = "",
                                updatedAt = "",
                                deadline = "",
                                dDay = -1,
                                finalAnswer = "",
                                review = WorryDetailEntity.Review(
                                    content = "",
                                    updatedAt = ""
                                )
                            )
                        )
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

    private fun setClickListeners() {
        with(binding.layoutError) {
            layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getHomeWorryList(false)
            }
            layoutInternalError.btnInternalError.onSingleClick {
                viewModel.getHomeWorryList(false)
            }
        }
    }

    // 서버 통신 1) 원석 (해결 전 고민들)
    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeWorryListStoneFlow.collect {
//                    Timber.e("[ABC] [홈 화면/원석 뷰] 수집 ${it.toString()}") // TODO: [231009] 글쓰기에서 X누르면 왜 안 와?
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
                homeStoneAdapter.submitList(uiState.data.homeWorryList.toMutableList())
            }

            is UiState.Error -> {
                controlLayout(success = false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root
                )
            }
        }
    }

    private fun controlLayout(success: Boolean, empty: Boolean = false) {
        binding.loadingBar.visible(false)
        binding.rvHomeStones.visible(success && !empty)
        binding.clEmpty.visible(success && empty)
        binding.layoutError.root.visible(!success)
    }

    /*-------------------------------------------------------------------------*/

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        Timber.e("[ABC] HomeStoneFragment - onViewStateRestored()")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Timber.e("[ABC] HomeStoneFragment - onResume()")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Timber.e("[ABC] HomeStoneFragment - onPause()")
//    }
//
//    override fun onDestroy() {
//        Timber.e("[ABC] HomeStoneFragment - onDestroy()")
//        super.onDestroy()
//    }

}