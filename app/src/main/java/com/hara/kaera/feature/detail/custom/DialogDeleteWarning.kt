package com.hara.kaera.feature.detail.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogWorryDeleteBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDeleteWarning(
    private val title: Int,
    private val subtitle: Int,
    private val yesClickListener: () -> Unit,
) : BindingDialogFragment<DialogWorryDeleteBinding>(R.layout.dialog_worry_delete, 16) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = requireActivity().getString(title)
        binding.subtitle = requireActivity().getString(subtitle)
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
