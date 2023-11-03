package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentRestartBinding
import com.hara.kaera.databinding.DialogFragmentSayingBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogRestartFragment(
    private val clickListener: () -> Unit,
):
    BindingDialogFragment<DialogFragmentRestartBinding>(R.layout.dialog_fragment_restart, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRestart.setOnClickListener {
            clickListener.invoke()
        }
    }

}