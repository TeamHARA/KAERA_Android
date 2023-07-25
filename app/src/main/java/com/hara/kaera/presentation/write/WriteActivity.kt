package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.onSingleClick
import timber.log.Timber

class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private val viewModel by viewModels<WriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTextWatcher()
        setClickListeners()
        addObserve()
    }

    private fun setTextWatcher() {
        binding.etTitle.addTextChangedListener {
            if(it?.length == 0) binding.tvTitleCount.text = "0/7"
            else binding.tvTitleCount.text = "${it!!.length}/7"
        }
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
        }
    }

    private fun addObserve() {
        viewModel.templateId.observe(this) {
            if (it == 0) {
                binding.clEmpty.root.visibility = View.GONE
                binding.clTemplate.root.visibility = View.GONE
                binding.clFreeflow.root.visibility = View.VISIBLE
            } else if (it in 1..6) {
                binding.clEmpty.root.visibility = View.GONE
                binding.clFreeflow.root.visibility = View.GONE
                binding.clTemplate.root.visibility = View.VISIBLE
            }
        }
    }

}