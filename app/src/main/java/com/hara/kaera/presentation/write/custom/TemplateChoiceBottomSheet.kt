package com.hara.kaera.presentation.write.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.BottomsheetTemplateChoiceBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.presentation.base.BindingDraggableBottomSheet
import com.hara.kaera.presentation.util.LastItemMarginItemDecoration
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.makeToast
import com.hara.kaera.presentation.util.visible
import com.hara.kaera.presentation.write.adapter.TemplateBottomSheetChoiceAdapter
import com.hara.kaera.presentation.write.viewmodel.TemplateChoiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TemplateChoiceBottomSheet(
    private val templateClickListener: (Int) -> Unit,
    private val selectedId: Int
) : BindingDraggableBottomSheet<BottomsheetTemplateChoiceBinding>(R.layout.bottomsheet_template_choice) {

    private lateinit var templateAdapter: TemplateBottomSheetChoiceAdapter
    private val viewModel by viewModels<TemplateChoiceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        templateAdapter = TemplateBottomSheetChoiceAdapter({
            templateClickListener(it)
            dismiss()
        }, selectedId)
        binding.rcvTemplate.apply {
            adapter = templateAdapter
            addItemDecoration(LastItemMarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.template_recyclerview_lastmargin)))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.templateStateFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }
    }

    private fun render(uiState: UiState<TemplateTypesEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO: 추가해주세요 (written by. 수현)
            is UiState.Loading -> {
                binding.loadingBar.visible(true)
            }

            is UiState.Success<TemplateTypesEntity> -> {
                binding.loadingBar.visible(false)
                templateAdapter.submitList(uiState.data.templateTypeList)
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }
        }
    }
}