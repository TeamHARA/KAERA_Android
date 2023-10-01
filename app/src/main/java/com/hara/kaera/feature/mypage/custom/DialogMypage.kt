package com.hara.kaera.feature.mypage.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogMypageBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogMypage(
    private val type: String,
    private val yesClickListener: () -> Unit,
) : BindingDialogFragment<DialogMypageBinding>(R.layout.dialog_mypage, 16) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setListeners()
    }

    private fun setLayout() {
        with(binding) {
            if (type == "logout") {
                tvDialogTitle.text = getString(R.string.logout_warning_title)
                tvDialogContent.text = getString(R.string.logout_warning_content)
                btnYes.text = getString(R.string.logout)
            } else {
                tvDialogTitle.text = getString(R.string.sign_out_warning_title)
                tvDialogContent.text = getString(R.string.sign_out_warning_content)
                btnYes.text = getString(R.string.sign_out_yes)
            }
        }
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
