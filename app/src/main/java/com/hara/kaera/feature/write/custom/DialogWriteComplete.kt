package com.hara.kaera.feature.write.custom

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentDeadlineBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DialogWriteComplete(
    private val onBtnCompleteListener: (day: Int) -> Unit,
    private val action: String,
    private val prevNum: Int
) : BindingDialogFragment<DialogFragmentDeadlineBinding>(R.layout.dialog_fragment_deadline,16)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnComplete.onSingleClick(1000) {
            onBtnCompleteListener(binding.pickerDeadlines.value)
            dismiss()
        }
        binding.btnNoDeadline.onSingleClick(1000) {
            onBtnCompleteListener(-1)
            dismiss()
        }
        //TODO POST 서버통신
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun setNumberPicker() {
        with(binding) {
            pickerDeadlines.apply {
                minValue = 1
                maxValue = 30
            }

            when(action) {
                "write" -> {
                    btnTitleFinish = "작성 완료"
                }
                "edit" -> {
                    btnTitleFinish = "수정 완료"
                    pickerDeadlines.value =
                        if (prevNum == -888) 1 // 무제한
                        else if (prevNum < 0) prevNum * -1
                        else if (prevNum == 0) 1 // D-day
                        else prevNum // 데드라인 경과
                }
            }
        }
    }

}