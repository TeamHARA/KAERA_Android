package com.hara.kaera.presentation.write.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentWirteCompleteBinding
import com.hara.kaera.presentation.base.BindingDialogFragment
import com.hara.kaera.presentation.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogWriteComplete :
    BindingDialogFragment<DialogFragmentWirteCompleteBinding>(
        R.layout.dialog_fragment_wirte_complete,
        16
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setNumberPicker()
    }

    private fun setOnClickListener() {
        binding.btnComplete.onSingleClick(1000) { }
        binding.btnNoDeadline.onSingleClick(1000) { }
        //TODO POST 서버통신
    }

    private fun setNumberPicker() {
        binding.pickerDeadlines.apply {
            minValue = 1
            maxValue = 30
        }
    }

}