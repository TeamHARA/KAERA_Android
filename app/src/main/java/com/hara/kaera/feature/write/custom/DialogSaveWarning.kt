package com.hara.kaera.feature.write.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentTemplatechangeWarningBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogSaveWarning(
    private val title: Int,
    private val yesClickListener: () -> Unit,
) : BindingDialogFragment<DialogFragmentTemplatechangeWarningBinding>(
    R.layout.dialog_fragment_templatechange_warning,
    16
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = requireActivity().getString(title)
        setListeners()
    }

    private fun setListeners() {
        binding.btnYes.setOnClickListener {
            this.dismiss()
            yesClickListener.invoke()
        }
        binding.btnNo.setOnClickListener {
            dismiss()
        }
    }

}

