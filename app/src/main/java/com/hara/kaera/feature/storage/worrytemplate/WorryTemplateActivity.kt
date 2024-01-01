package com.hara.kaera.feature.storage.worrytemplate

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWorryTemplateBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WorryTemplateActivity :
    BindingActivity<ActivityWorryTemplateBinding>(R.layout.activity_worry_template) {
    private lateinit var worryTemplateAdapter: WorryTemplateAdapter
    private val viewModel by viewModels<WorryTemplateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setOnClickListeners()
        collectFlows()
    }

    private fun setOnClickListeners() {
        with(binding.btnClose) {
            increaseTouchSize(baseContext)
            setOnClickListener { finish() }
        }
        with(binding.layoutError) {
            layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getWorryTemplates()
            }
            layoutInternalError.btnInternalError.onSingleClick {
                viewModel.getWorryTemplates()
            }
        }

    }

    private fun initAdapter() {
        worryTemplateAdapter = WorryTemplateAdapter { templateId ->
            startActivity(
                Intent(this, WriteActivity::class.java).apply {
                    putExtra("templateId", templateId)
                    putExtra("action", "write")
                },
            )
            finish()
        }
        binding.rcvWorryTemplate.adapter = worryTemplateAdapter
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.worryTemplateStateFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }
    }

    private fun render(uiState: UiState<TemplateTypesEntity>) {
        when (uiState) {
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success<TemplateTypesEntity> -> {
                controlLayout(true)
                worryTemplateAdapter.submitList(uiState.data.templateTypeList)
            }

            is UiState.Error -> {
                controlLayout(false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root
                )
            }

            else -> {
                Timber.e("else")
            }
        }
    }

    private fun controlLayout(success: Boolean) {
        binding.layoutLoading.root.visible(false)
        binding.layoutError.root.visible(!success)
        binding.rcvWorryTemplate.visible(success)
    }
}
