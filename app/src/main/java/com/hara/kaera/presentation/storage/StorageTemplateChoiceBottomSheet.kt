package com.hara.kaera.presentation.write

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.BottomsheetTemplateChoiceBinding
import com.hara.kaera.presentation.base.BindingDraggableBottomSheet
import com.hara.kaera.presentation.storage.data.DummyStorageTemplateData
import com.hara.kaera.presentation.util.LastItemMarginItemDecoration
import com.hara.kaera.presentation.write.adapter.StorageTemplateChoiceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageTemplateChoiceBottomSheet(
    private val templateClickListener: (Int) -> Unit,
    private val onDismissed: () -> Unit,
) : BindingDraggableBottomSheet<BottomsheetTemplateChoiceBinding>(R.layout.bottomsheet_template_choice) {

    private lateinit var storageTemplateAdapter: StorageTemplateChoiceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageTemplateAdapter = StorageTemplateChoiceAdapter {
            templateClickListener(it)
        }
        binding.rcvTemplate.apply {
            adapter = storageTemplateAdapter
            addItemDecoration(LastItemMarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.template_recyclerview_lastmargin)))
        }
        storageTemplateAdapter.submitList(DummyStorageTemplateData.templateList)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissed()
    }
}
