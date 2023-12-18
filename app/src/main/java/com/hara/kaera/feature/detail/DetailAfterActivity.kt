package com.hara.kaera.feature.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityDetailAfterBinding
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.detail.custom.DialogDeleteWarning
import com.hara.kaera.feature.detail.custom.DialogUpdateWarning
import com.hara.kaera.feature.dialog.DialogWriteSuccess
import com.hara.kaera.feature.util.SetKeyboard
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailAfterActivity :
    BindingActivity<ActivityDetailAfterBinding>(R.layout.activity_detail_after) {
    private val viewModel by viewModels<DetailAfterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectFlows()
        getWorryById()
        setClickListener()
        setKeyboardListener()
    }

    override fun onBackPressed() {
        onClickBackPressed()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { // 현재 포커싱 이외의 영역 클릭 시 키보드 제거
        if (ev.action == MotionEvent.ACTION_UP) {
            val view = currentFocus
            if (view != null && view != binding.tvSaveBtn) { // 저장 버튼을 클릭한 경우는 제외
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    SetKeyboard.hideSoftKeyboard(this, view.windowToken)
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun getWorryById() {
        val worryId = intent.getIntExtra("worryId", 0)
        viewModel.getWorryDetail(worryId)

        // 에러레이아웃에서 쓰일 버튼
        binding.layoutError.layoutNetworkError.btnNetworkError.onSingleClick {
            viewModel.getWorryDetail(worryId)
        }
        binding.layoutError.layoutInternalError.btnInternalError.onSingleClick {
            viewModel.getWorryDetail(worryId)
        }

    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.detailStateFlow.collect {
                        renderGetWorry(it)
                    }
                }
                launch {
                    viewModel.deleteWorryFlow.collect {
                        renderDeleteWorry(it)
                    }
                }
                launch {
                    viewModel.reviewWorryFlow.collect {
                        renderUpdateReview(it)
                    }
                }
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            appbarDetail.setNavigationOnClickListener { // 앱바 'X' 버튼 클릭
                onClickBackPressed()
            }
            btnDelete.onSingleClick {
                DialogDeleteWarning {
                    viewModel.deleteWorry()
                }.show(supportFragmentManager, "delete_worry")
            }
            tvSaveBtn.onSingleClick {
                viewModel.updateReview(binding.etRecordContent.text.toString())
                etRecordContent.clearFocus()
                SetKeyboard.hideSoftKeyboard(applicationContext, tvSaveBtn.windowToken)
            }
            clRecord.onSingleClick {
                etRecordContent.setSelection(etRecordContent.text.length)
                SetKeyboard.showSoftKeyboard(applicationContext, etRecordContent)
            }
        }
    }

    private fun onClickBackPressed() {
        if (viewModel.reviewContent != binding.etRecordContent.text.toString()) {
            DialogUpdateWarning {
                finish()
            }.show(supportFragmentManager, "update_review")
        } else {
            finish()
        }
    }

    private fun renderDeleteWorry(uiState: UiState<DeleteWorryEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                finish()
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun renderUpdateReview(uiState: UiState<ReviewResEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                binding.layoutLoading.root.visible(false)

                // update review date
                binding.tvRecordDate.text = uiState.data.updateDate
                DialogWriteSuccess().show(supportFragmentManager, "review")
                // update current review
                viewModel.reviewContent = binding.etRecordContent.text.toString()
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun renderGetWorry(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)


            is UiState.Success<WorryDetailEntity> -> {
                controlErrorLayout(success = true)

                val worryDetail = uiState.data
                binding.worryDetail = worryDetail

                if (worryDetail.templateId == 1) { // freeflow
                    with(binding) {
                        layoutAnswer2.root.visible(false)
                        layoutAnswer3.root.visible(false)
                        layoutAnswer4.root.visible(false)
                    }
                } else {
                    val layouts =
                        listOf(binding.layoutAnswer2, binding.layoutAnswer3, binding.layoutAnswer4)
                    layouts.forEachIndexed { index, layout ->
                        if (index < worryDetail.subtitles.size && index < worryDetail.answers.size) {
                            layout.tvTitle.text = worryDetail.subtitles[index + 1]
                            layout.tvContent.text = worryDetail.answers[index + 1]
                        }
                    }
                }
                // update 된 review 반영
                viewModel.reviewContent = worryDetail.review?.content.toString()
            }

            is UiState.Error -> controlErrorLayout(uiState.error, false)

            UiState.Empty -> TODO()
        }
    }

    private fun controlErrorLayout(error: String? = null, success: Boolean) {
        binding.layoutLoading.root.visible(false)
        if (success) {
            binding.layoutError.layoutNetworkError.root.visible(false)
            binding.layoutError.layoutInternalError.root.visible(false)
        } else {
            binding.svContent.visible(false)
            when (error) {
                Constant.networkError -> {
                    binding.layoutError.layoutNetworkError.root.visible(true)
                }

                Constant.internalError -> {
                    binding.layoutError.layoutInternalError.root.visible(true)
                }
            }
        }
    }

    private fun setKeyboardListener() {
        val rootView = window.decorView.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            rootView.getWindowVisibleDisplayFrame(rec)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rec.bottom

            if (keypadHeight > screenHeight * 0.15) { // 키보드가 표시된 경우
                binding.clSaveLayout.visibility = View.VISIBLE
                binding.svContent.post {
                    binding.svContent.fullScroll(View.FOCUS_DOWN)
                }
            } else { // 키보드가 숨겨진 경우
                binding.clSaveLayout.visibility = View.GONE
            }
        }
    }
}
