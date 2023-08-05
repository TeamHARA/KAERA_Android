package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.makeSnackBar
import com.hara.kaera.presentation.util.onSingleClick
import timber.log.Timber

class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {


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
        addObserve()
    }

    private fun setClickListeners() {
        binding.apply {
            appbarDetail.setNavigationOnClickListener {
                finish()
            }
            clChoice.onSingleClick(1000) {
                TemplateChoiceBottomSheet(Mode.WRITE, {
                    viewModel.setTemplateId(it)
                    Timber.d(viewModel.templateId.value.toString())
                }, {}).show(supportFragmentManager, "template_choice")
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