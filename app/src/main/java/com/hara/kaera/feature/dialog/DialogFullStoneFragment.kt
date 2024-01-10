package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentFullstoneBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogFullStoneFragment :
    BindingDialogFragment<DialogFragmentFullstoneBinding>(R.layout.dialog_fragment_fullstone, 16) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnComplete.setOnClickListener {
            dismiss()
        }
    }

}