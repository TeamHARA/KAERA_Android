package com.hara.kaera.feature.detail.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentTemplatechangeWarningBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogUpdateWarning(
    private val yesClickListener: () -> Unit,
) : BindingDialogFragment<DialogFragmentTemplatechangeWarningBinding>(R.layout.dialog_fragment_templatechange_warning, 16) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnYes.onSingleClick {
            this.dismiss()
            yesClickListener.invoke()
        }
        binding.btnNo.onSingleClick() {
            dismiss()
        }
    }
}
