package com.hara.kaera.feature.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityDetailBeforeBinding
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailBeforeActivity :
    BindingActivity<ActivityDetailBeforeBinding>(R.layout.activity_detail_before) {
    private val viewModel by viewModels<DetailBeforeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWorryById()
        collectFlows()
    }

    private fun getWorryById() {
        val worryId = intent.getIntExtra("worryId", 0)
        Timber.e("가져온 worryId ${worryId}")
        viewModel.getWorryDetail(worryId)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.detailStateFlow.collect {
                        Timber.e("render하러 감요")
                        render(it)
                    }
                }
//                launch {
//                    viewModel.deleteWorryFlow.collect {
//                        renderDelete(it)
//                    }
//                }
//                launch {
//                    viewModel.reviewWorryFlow.collect {
//                        renderUpdateReviewDate(it)
//                    }
//                }
            }
        }
    }

    private fun render(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> Unit
            is UiState.Success<WorryDetailEntity> -> {
                val worryDetail = uiState.data
                binding.worryDetail = worryDetail
                Timber.e("HI" + worryDetail.toString())

                if (worryDetail.templateId == 1) { // free flow
                    binding.cvContent.visibility = View.GONE
                    binding.frCvContent.visibility = View.VISIBLE
                    Timber.e("free flow 화면 왜 안 나와!")
                } else {
                    binding.cvContent.visibility = View.VISIBLE
                    binding.frCvContent.visibility = View.GONE
                    Timber.e("일반 상세 화면 왜 안 나와!")
                }
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }
}