package com.hara.kaera.presentation.write

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.BottomsheetTemplateChoiceBinding
import com.hara.kaera.presentation.base.BindingDraggableBottomSheet
import com.hara.kaera.presentation.util.LastItemMarginItemDecoration
import com.hara.kaera.presentation.write.adapter.TemplateChoiceAdapter
import com.hara.kaera.presentation.write.data.DummyTemplateData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemplateChoiceBottomSheet(
    private val templateClickListener: (Int) -> Unit,
    private val selectedId: Int,
    onDismissed: () -> Unit,
    private val mode: Mode,
) : BindingDraggableBottomSheet<BottomsheetTemplateChoiceBinding>(R.layout.bottomsheet_template_choice) {

    private lateinit var templateAdapter: TemplateChoiceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        templateAdapter = TemplateChoiceAdapter({
            templateClickListener(it)
            dismiss()
        }, selectedId, mode)
        binding.rcvTemplate.apply {
            adapter = templateAdapter
            addItemDecoration(LastItemMarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.template_recyclerview_lastmargin)))
        }
        when (mode) {
            Mode.WRITE -> {
                templateAdapter.submitList(DummyTemplateData.templateList.subList(1, 8))
            }

            Mode.STORAGE -> {
                templateAdapter.submitList(DummyTemplateData.templateList)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissed()
    }
}

enum class Mode {
    WRITE,
    STORAGE,
}