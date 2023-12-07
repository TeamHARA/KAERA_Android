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
    }
}
