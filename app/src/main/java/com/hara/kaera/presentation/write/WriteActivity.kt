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
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.onSingleClick
import com.hara.kaera.presentation.write.viewmodel.TestViewModel
import com.hara.kaera.presentation.write.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private val testViewModel: TestViewModel by viewModels()

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeFlow: EditText
    private lateinit var edittextTitle: EditText

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.templateDetailFlow.collect {
                    render(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.templateStateFlow.collect {
                    render(it)
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
                Timber.e(uiState.data.toString())
            }

            is UiState.Error -> {
                Timber.e(uiState.message)
            }
        }

        addObserve()
    }


    private fun setClickListeners() {
        binding.apply {
            appbarDetail.setNavigationOnClickListener {
                finish()
            }
            clChoice.onSingleClick(1000) {
                TemplateChoiceBottomSheet() {
                    viewModel.setTemplateId(it)
                    Timber.d(viewModel.templateId.value.toString())
                }.show(supportFragmentManager, "template_choice")
            }
            btnComplete.onSingleClick(1000) {
                Timber.e("complete")
            }
        }
    }

    private fun setTextWatcher() {
        edittextTitle.addTextChangedListener {
            if (it?.length == 0) binding.tvTitleCount.text = "0/7"
            else binding.tvTitleCount.text = "${it!!.length}/7"

            if (viewModel.templateId.value == 0) checkFreeFlow()
            else checkTemplate()
        }
        editTextFreeFlow.addTextChangedListener {
            checkFreeFlow()
        }
        editTextList.forEach {
            it.addTextChangedListener { checkTemplate() }
        }

    }

    private fun addObserve() {
        viewModel.templateId.observe(this) {
            if (it == 0) { // freeflow
                binding.clEmpty.root.visibility = View.GONE
                binding.clTemplate.root.visibility = View.GONE
                binding.clFreeflow.root.visibility = View.VISIBLE
            } else if (it in 1..6) { // freeflow 제외 나머지
                binding.clEmpty.root.visibility = View.GONE
                binding.clFreeflow.root.visibility = View.GONE
                binding.clTemplate.root.visibility = View.VISIBLE
            }
            clearEditText()
        }
    }

    private fun clearEditText() {
        editTextList.forEach {
            it.text.clear()
        }
        editTextFreeFlow.text.clear()
    }

    private fun checkFreeFlow() {
        binding.btnComplete.isActivated =
            editTextFreeFlow.text.isNotEmpty() && edittextTitle.text.isNotEmpty()
    }

    private fun checkTemplate() {
        binding.btnComplete.isActivated =
            editTextList.all { it.text.isNotEmpty() } && edittextTitle.text.isNotEmpty()
    }

}