package com.hara.kaera.feature.splash

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
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

        dialog!!.setOnKeyListener { dialog: DialogInterface?, keyCode: Int, event: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                noClickListener.invoke()
                dismiss()
                return@setOnKeyListener true
            }
            false
        }
    }

}