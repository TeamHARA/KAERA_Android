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
import com.hara.kaera.feature.dialog.DialogEditFragment
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailBeforeActivity :
    BindingActivity<ActivityDetailBeforeBinding>(R.layout.activity_detail_before) {
    private val viewModel by viewModels<DetailBeforeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWorryById()
        collectFlows()
        setClickListener()
    }

    private fun getWorryById() {
        val worryId = intent.getIntExtra("worryId", 0)
        viewModel.getWorryDetail(worryId)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.detailStateFlow.collect {
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

                if (worryDetail.templateId == 1) { // free flow
                    binding.cvContent.visibility = View.GONE
                    binding.frCvContent.visibility = View.VISIBLE
                } else {
                    binding.cvContent.visibility = View.VISIBLE
                    binding.frCvContent.visibility = View.GONE
                }
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun setClickListener() {
        with(binding) { // 앱바 'X' 버튼 클릭
            appbarDetail.setNavigationOnClickListener {
                finish()
            }
            btnEdit.setOnClickListener {// 햄버거 버튼
                DialogEditFragment().show(supportFragmentManager, "edit")
            }
            btnSubmit.setOnClickListener { // 하단 "고민 보석 캐기" 버튼

            }
        }
    }
}