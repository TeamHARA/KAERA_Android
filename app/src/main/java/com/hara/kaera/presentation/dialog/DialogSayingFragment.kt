package com.hara.kaera.presentation.dialog

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentSayingBinding
import com.hara.kaera.presentation.base.BindingDialogFragment

class DialogSayingFragment :
    BindingDialogFragment<DialogFragmentSayingBinding>(R.layout.dialog_fragment_saying, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}