package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentWriteSuccessBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogWriteSuccess : BindingDialogFragment<DialogFragmentWriteSuccessBinding>(
    R.layout.dialog_fragment_write_success,
    44,
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val fadeOut = AlphaAnimation(1.0f, 0.0f)
            fadeOut.duration = 1000 // 애니메이션 지속 시간: 1초

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    dismiss()
                }

                override fun onAnimationStart(animation: Animation?) {}
            })

            view.startAnimation(fadeOut)
        }, 1000)
    }
}
