package com.hara.kaera.feature.write.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentBackpressWarningBinding
import com.hara.kaera.databinding.DialogFragmentTemplatechangeWarningBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogBackpressWarning(
    private val yesClickListener: () -> Unit
) : BindingDialogFragment<DialogFragmentBackpressWarningBinding>(
    R.layout.dialog_fragment_backpress_warning,
    16
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnYes.setOnClickListener {
            this.dismiss()
            yesClickListener.invoke()
        }
        binding.btnNo.onSingleClick() {
            dismiss()
        }
    }

}