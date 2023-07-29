package com.hara.kaera.presentation.dialog

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentCompleteBinding
import com.hara.kaera.presentation.base.BindingDialogFragment

class DialogCompleteFragment :
    BindingDialogFragment<DialogFragmentCompleteBinding>(R.layout.dialog_fragment_complete, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

}