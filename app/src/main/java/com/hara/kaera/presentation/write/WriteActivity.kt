package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.makeSnackBar
import com.hara.kaera.presentation.util.onSingleClick
import com.hara.kaera.presentation.util.stringOf
import com.hara.kaera.presentation.write.custom.TemplateChoiceBottomSheet
import com.hara.kaera.presentation.write.custom.DialogSaveWarning
import com.hara.kaera.presentation.write.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeFlow: EditText
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
        editTextFreeFlow = binding.clFreeflow.etFreeflow
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
                if (titleCondition || contentCondition) { // 한글자라도 써놨을 경우
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
                if (!titleCondition) binding.root.makeSnackBar(baseContext.stringOf(R.string.write_title_error))
                else if (!contentCondition) binding.root.makeSnackBar(baseContext.stringOf(R.string.write_content_error))
                else Timber.e("굿") //TODO post 서버통신
            }
        }
    }

    private fun setTextWatcher() {
        edittextTitle.addTextChangedListener {
            binding.tvTitleCount.text =
                String.format(this.stringOf(R.string.write_title_count), it!!.length)
            if (viewModel.templateIdFlow.value == 0) checkFreeFlow()
            else checkTemplate()
        }
        editTextFreeFlow.addTextChangedListener {
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.templateDetailFlow.collect {
                    render(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.templateIdFlow.collect {
                    if (it in 1..6) viewModel.getTemplateDetailData()
                }
            }
        }
        // 마찬가지로 코루틴 열고 수집을 하는데 생명주기에 맞춰서 flow가 자동으로
        // 꺼지고 수집하고 될수 있도록 repeatOnLifeCycle이란걸 사용! 그리고
        // 뷰모델에 있는 StateFlow에서 뷰모델에서 해줬던것처럼 collect 해준다/
        // 여기 내부 값은 UiState가 들어오게 된다!
    }

    private fun render(uiState: UiState) {
        // 실제로 뷰에서 대응하는 함수 프로그래스바  visibility조절, 에러메시지 출력등을 하면 된다!
        when (uiState) {
            is UiState.Loading -> {//TODO
            }

            is UiState.Success<*> -> {
                if (viewModel.templateIdFlow.value == 1) { // freeflow
                    binding.templatedata = uiState.data as TemplateDetailEntity.TemplateDetailInfo
                    binding.clFreeflow.templatedata =
                        uiState.data as TemplateDetailEntity.TemplateDetailInfo
                    binding.clEmpty.root.visibility = View.GONE
                    binding.clTemplate.root.visibility = View.GONE
                    binding.clFreeflow.root.visibility = View.VISIBLE
                } else if (viewModel.templateIdFlow.value in 2..6) { // freeflow 제외 나머지
                    binding.templatedata = uiState.data as TemplateDetailEntity.TemplateDetailInfo
                    binding.clTemplate.templatedata =
                        uiState.data as TemplateDetailEntity.TemplateDetailInfo
                    binding.clEmpty.root.visibility = View.GONE
                    binding.clFreeflow.root.visibility = View.GONE
                    binding.clTemplate.root.visibility = View.VISIBLE
                    if (viewModel.templateIdFlow.value == 5) binding.clTemplate.tvThanks.visibility =
                        View.VISIBLE //thanksTo 템플릿
                    else binding.clTemplate.tvThanks.visibility = View.GONE
                }
                clearEditText()
            }

            is UiState.Error -> {
                Timber.e(uiState.message)
            }
        }
    }

    private fun clearEditText() {
        editTextList.forEach {
            it.text.clear()
        }
        editTextFreeFlow.text.clear()
        edittextTitle.text.clear()
    }

    private fun checkFreeFlow() {
        contentCondition = editTextFreeFlow.text.isNotEmpty() && editTextFreeFlow.text.isNotBlank()
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }

    private fun checkTemplate() {
        contentCondition = editTextList.all { it.text.isNotEmpty() && it.text.isNotBlank() }
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }


}