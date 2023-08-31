package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentWorryMiningBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogCompleteFragment :
    BindingDialogFragment<DialogFragmentWorryMiningBinding>(R.layout.dialog_fragment_worry_mining, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.etComplete.addTextChangedListener {
            binding.btnComplete.isEnabled = it!!.isNotBlank() && it!!.isNotEmpty()
        }

    }

}