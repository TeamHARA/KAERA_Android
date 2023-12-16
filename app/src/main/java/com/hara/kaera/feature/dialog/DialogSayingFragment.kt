package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentSayingBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogSayingFragment(
    private val templateId: Int,
    private val saying: String
) :
    BindingDialogFragment<DialogFragmentSayingBinding>(R.layout.dialog_fragment_saying, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.templateId = templateId // 원석 이미지 설정을 위함
        binding.tvSaying.text = saying
        
        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        }, 2000) // 애니메이션 시작전 2000ms 후 자동종료
    }

}