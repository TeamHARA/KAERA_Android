package com.hara.kaera.feature.write

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.BottomsheetTemplateChoiceBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.feature.base.BindingDraggableBottomSheet
import com.hara.kaera.feature.storage.adapter.StorageTemplateChoiceAdapter
import com.hara.kaera.feature.storage.viewmodel.StorageTemplateChoiceViewModel
import com.hara.kaera.feature.util.LastItemMarginItemDecoration
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StorageTemplateChoiceBottomSheet(
    private val templateClickListener: (Int, String) -> Unit,
    private val selectedId: Int,
    private val onDismissed: () -> Unit,
) : BindingDraggableBottomSheet<BottomsheetTemplateChoiceBinding>(R.layout.bottomsheet_template_choice) {

    private lateinit var storageTemplateAdapter: StorageTemplateChoiceAdapter
    private val viewModel by viewModels<StorageTemplateChoiceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageTemplateAdapter = StorageTemplateChoiceAdapter({ templateId, title ->
            templateClickListener(templateId, title)
            dismiss()
        }, selectedId)
        binding.rcvTemplate.apply {
            adapter = storageTemplateAdapter
            addItemDecoration(LastItemMarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.template_recyclerview_lastmargin)))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.templateStateFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }

        with(binding.layoutError) {
            layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getTemplateList()
            }
            layoutInternalError.btnInternalError.onSingleClick {
                viewModel.getTemplateList()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissed()
    }

    private fun render(uiState: UiState<TemplateTypesEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit
            is UiState.Loading -> binding.loadingBar.root.visible(true)

            is UiState.Success<TemplateTypesEntity> -> {
                controlLayout(true)
                storageTemplateAdapter.submitList(uiState.data.templateTypeList)
            }

            is UiState.Error -> {
                controlLayout(false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    binding.root
                )
            }
        }
    }

    private fun controlLayout(success: Boolean) {
        binding.loadingBar.root.visible(false)
        binding.rcvTemplate.visible(success)
        binding.layoutError.root.visible(!success)
    }

}
