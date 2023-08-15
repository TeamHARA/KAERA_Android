package com.hara.kaera.presentation.write.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogWarningBinding
import com.hara.kaera.presentation.base.BindingDialogFragment
import com.hara.kaera.presentation.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogSaveWarning(
    private val yesClickListener: () -> Unit
) : BindingDialogFragment<DialogWarningBinding>(R.layout.dialog_warning, 16) {
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

