package com.hara.kaera.feature.splash

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentAppUpdateBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogUpdateFragment(
    private val title: String,
    private val yesClickListener: () -> Unit,
    private val noClickListener: () -> Unit,
) :
    BindingDialogFragment<DialogFragmentAppUpdateBinding>(R.layout.dialog_fragment_app_update, 16) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = title
        binding.btnYes.setOnClickListener {
            yesClickListener.invoke()
        }
        binding.btnNo.setOnClickListener {
            noClickListener.invoke()
            dismiss()
        }
    }

}