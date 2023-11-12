package com.hara.kaera.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentRestartBinding
import com.hara.kaera.feature.base.BindingDialogFragment


class DialogRestartFragment(
    private val clickListener: () -> Unit,
) :
    BindingDialogFragment<DialogFragmentRestartBinding>(R.layout.dialog_fragment_restart, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false) // 뒷배경 터치시 종료 방지
        binding.btnRestart.setOnClickListener {
            clickListener.invoke()
            dismiss()
        }
        // 뒤로가기의 경우 activity를 종료
        dialog!!.setOnKeyListener { dialog: DialogInterface?, keyCode: Int, event: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                requireActivity().finish()
                return@setOnKeyListener true
            }
            false
        }
    }

}