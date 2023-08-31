package com.hara.kaera.feature.write

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.dialog.DialogCompleteFragment
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.custom.DialogSaveWarning
import com.hara.kaera.feature.write.custom.TemplateChoiceBottomSheet
import com.hara.kaera.feature.write.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeNote: EditText
    private lateinit var edittextTitle: EditText

    private var titleCondition = false
    private var contentCondition = false

    private val viewModel by viewModels<WriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editTextList = listOf<EditText>(
            binding.clTemplate.etAnswer1,
            binding.clTemplate.etAnswer2,
            binding.clTemplate.etAnswer3,
            binding.clTemplate.etAnswer4
        )
        editTextFreeNote = binding.clFreenote.etFreenote
        edittextTitle = binding.etTitle

        setTextWatcher()
        setClickListeners()
        collectFlows()
    }

    private fun setClickListeners() {
        binding.apply {
            appbarDetail.setNavigationOnClickListener {
                finish()
            }
            clChoice.onSingleClick {
                if (checkText()) { // 한글자라도 써놨을 경우
                    DialogSaveWarning {
                        TemplateChoiceBottomSheet({
                            viewModel.setTemplateId(it)
                        }, viewModel.templateIdFlow.value).show(
                            supportFragmentManager,
                            "template_choice"
                        )
                    }.show(supportFragmentManager, "warning")
                } else {
                    TemplateChoiceBottomSheet({
                        viewModel.setTemplateId(it)
                    }, viewModel.templateIdFlow.value).show(
                        supportFragmentManager,
                        "template_choice"
                    )
                }
            }

            btnComplete.onSingleClick(1000) {
                if (!titleCondition) KaeraSnackBar.make(
                    binding.root, baseContext.stringOf(R.string.write_snackbar_title),
                    KaeraSnackBar.DURATION.LONG
                ).show()
                else if (!contentCondition) KaeraSnackBar.make(
                    binding.root,
                    baseContext.stringOf(R.string.write_snackbar_content),
                    KaeraSnackBar.DURATION.LONG
                ).show()
                else {
                    DialogWriteComplete() {}.show(supportFragmentManager, "complete")
                }
            }
        }
    }

    private fun setTextWatcher() {
        edittextTitle.addTextChangedListener {
            binding.tvTitleCount.text =
                String.format(this.stringOf(R.string.write_title_count), it!!.length)
            if (viewModel.templateIdFlow.value == 1) checkFreeFlow()
            else checkTemplate()
        }
        editTextFreeNote.addTextChangedListener {
            checkFreeFlow()
        }
        editTextList.forEach {
            it.addTextChangedListener { checkTemplate() }
        }
        editTextList[3].addTextChangedListener {
            binding.clTemplate.tvThanks.text =
                String.format(this.stringOf(R.string.write_thanksto), it)
        }
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.templateIdFlow.collect {
                        if (it in 1..6 && it != viewModel.curTemplateIdFlow.value) {
                            viewModel.getTemplateDetailData()
                        }
                    }
                }
                launch {
                    viewModel.templateDetailFlow.collect {
                        if (viewModel.templateIdFlow.value != viewModel.curTemplateIdFlow.value)
                            render(it)
                    }
                }
            }
        }
        // 마찬가지로 코루틴 열고 수집을 하는데 생명주기에 맞춰서 flow가 자동으로
        // 꺼지고 수집하고 될수 있도록 repeatOnLifeCycle이란걸 사용! 그리고
        // 뷰모델에 있는 StateFlow에서 뷰모델에서 해줬던것처럼 collect 해준다/
        // 여기 내부 값은 UiState가 들어오게 된다!
    }

    private fun render(uiState: UiState<TemplateDetailEntity>) {
        // 실제로 뷰에서 대응하는 함수 프로그래스바  visibility조절, 에러메시지 출력등을 하면 된다!
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO: 추가해주세요 (written by. 수현)
            is UiState.Loading -> {
                binding.loadingBar.visible(true)
            }

            is UiState.Success -> {
                binding.loadingBar.visible(false)
                binding.clTitle.visible(true)
                if (viewModel.templateIdFlow.value == 1) { // freenote
                    binding.templatedata = uiState.data
                    binding.clEmpty.root.visible(false)
                    binding.clTemplate.root.visible(false)
                    binding.clFreenote.root.visible(true)
                } else if (viewModel.templateIdFlow.value in 2..6) { // freenote 제외 나머지
                    binding.templatedata = uiState.data
                    binding.clEmpty.root.visible(false)
                    binding.clFreenote.root.visible(false)
                    binding.clTemplate.root.visible(true)
                    if (viewModel.templateIdFlow.value == 5) binding.clTemplate.tvThanks.visible(
                        true
                    ) //thanksTo 템플릿
                    else binding.clTemplate.tvThanks.visible(false)
                }
                clearEditText()
            }

            is UiState.Error -> {
                //TODO 에러뷰 표시
                binding.root.makeToast(uiState.error)
            }
        }
    }

    private fun clearEditText() {
        editTextList.forEach {
            it.text.clear()
        }
        editTextFreeNote.text.clear()
        edittextTitle.text.clear()
        //TODO condtion 변수들 초기화
    }

    private fun checkFreeFlow() {
        contentCondition = editTextFreeNote.text.isNotEmpty() && editTextFreeNote.text.isNotBlank()
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }

    private fun checkTemplate() {
        contentCondition = editTextList.all { it.text.isNotEmpty() && it.text.isNotBlank() }
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }

    private fun checkText(): Boolean =
        (titleCondition) || (editTextList.any { it.text.isNotEmpty() && it.text.isNotBlank() }) || (editTextFreeNote.text.isNotEmpty() || editTextFreeNote.text.isNotEmpty() && editTextFreeNote.text.isNotBlank())

    override fun onStop() {
        super.onStop()
        viewModel.setCurTemplateId(viewModel.templateIdFlow.value)
        // 백그라운드 전환시 render 재실행으로 인한 텍스트초기화를 막기위해 flow에 현재 id 저장
    }

}