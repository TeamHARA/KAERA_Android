package com.hara.kaera.feature.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.SetKeyboard
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.scrollableInScrollView
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class DetailAfterActivity :
    BindingActivity<ActivityDetailAfterBinding>(R.layout.activity_detail_after) {

    private val viewModel by viewModels<DetailAfterViewModel>()

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onClickBackPressed()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)
        collectFlows()
        getWorryById()
        setClickListener()
        setKeyboardListener()
        Timber.e(binding.etRecordContent.text.toString())
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
        val worryId = intent.getIntExtra(Constant.worryIdIntent, 0)
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
            with(btnClose) {
                increaseTouchSize(baseContext)
                setOnClickListener { // 앱바 'X' 버튼 클릭
                    onClickBackPressed()
                }
            }
            with(btnDelete) {
                increaseTouchSize(baseContext)
                onSingleClick {
                    DialogDeleteWarning(
                        title = R.string.dialog_after_delete_title,
                        subtitle = R.string.dialog_after_delete_subtitle
                    ) {
                        viewModel.deleteWorry()
                    }.show(supportFragmentManager, "delete_worry")
                }
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
            etRecordContent.scrollableInScrollView()
        }
    }

    private fun onClickBackPressed() {
        Timber.e(viewModel.getReviewContent() + " , " + binding.etRecordContent.text.toString())
        if (viewModel.getReviewContent() != binding.etRecordContent.text.toString()) {
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
            is UiState.Empty -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                finish()
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

        }
    }

    private fun renderUpdateReview(uiState: UiState<ReviewResEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                binding.layoutLoading.root.visible(false)
                binding.tvRecordDate.text = uiState.data.updateDate
                DialogWriteSuccess().show(supportFragmentManager, "review")
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

        }
    }

    private fun renderGetWorry(uiState: UiState<WorryDetailEntity>) {
        Timber.e(uiState.toString())
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success<WorryDetailEntity> -> {
                controlLayout(success = true)

                binding.worryDetail = uiState.data

                if (binding.worryDetail!!.templateId == Constant.freeNoteId) { // freeNote
                    with(binding) {
                        layoutAnswer2.root.visible(false)
                        layoutAnswer3.root.visible(false)
                        layoutAnswer4.root.visible(false)
                    }
                } else {
                    val layouts =
                        listOf(binding.layoutAnswer2, binding.layoutAnswer3, binding.layoutAnswer4)
                    layouts.forEachIndexed { index, layout ->
                        if (index < binding.worryDetail!!.subtitles.size && index < binding.worryDetail!!.answers.size) {
                            layout.tvTitle.text = binding.worryDetail!!.subtitles[index + 1]
                            layout.tvContent.text = binding.worryDetail!!.answers[index + 1]
                        }
                    }
                }
            }

            is UiState.Error -> {
                controlLayout(false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    root = binding.root
                )
            }

        }
    }

    private fun controlLayout(success: Boolean) {
        binding.layoutLoading.root.visible(false)
        binding.svContent.visible(success)
        binding.layoutError.root.visible(!success)
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
