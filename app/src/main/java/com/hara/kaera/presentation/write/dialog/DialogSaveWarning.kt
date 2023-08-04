package com.hara.kaera.presentation.write.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hara.kaera.databinding.DialogWarningBinding
import com.hara.kaera.presentation.util.onSingleClick

class DialogSaveWarning(
    private val yesClickListener: () -> Unit
) : DialogFragment() {
    //TODO BindingDialogFragment 적용
    private lateinit var binding: DialogWarningBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogWarningBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
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

