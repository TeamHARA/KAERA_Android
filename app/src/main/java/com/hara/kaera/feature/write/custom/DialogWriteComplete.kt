package com.hara.kaera.feature.write.custom

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentDeadlineBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.write.WriteActivity.Companion.ACTION_EDIT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogWriteComplete(
    private val onBtnCompleteListener: (day: Int) -> Unit,
    private val action: String,
    private val prevNum: Int
) : BindingDialogFragment<DialogFragmentDeadlineBinding>(R.layout.dialog_fragment_deadline, 16) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnComplete.onSingleClick {
            onBtnCompleteListener(binding.pickerDeadlines.value)
            dismiss()
        }
        binding.btnNoDeadline.onSingleClick {
            onBtnCompleteListener(-1)
            dismiss()
        }
    }

    private fun setNumberPicker() {
        with(binding) {
            pickerDeadlines.apply {
                minValue = 1
                maxValue = 30
            }

            if (action == ACTION_EDIT) {
                pickerDeadlines.value =
                    if (prevNum == -888) 1 // 무제한
                    else if (prevNum < 0) prevNum * -1
                    else if (prevNum == 0) 1 // D-day
                    else prevNum // 데드라인 경과
            }
        }
    }

}