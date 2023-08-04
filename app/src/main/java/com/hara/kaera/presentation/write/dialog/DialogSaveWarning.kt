package com.hara.kaera.presentation.write.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hara.kaera.databinding.DialogWarningBinding
import com.hara.kaera.presentation.util.onSingleClick

class DialogSaveWarning : DialogFragment() {
    //TODO BindingDialogFragment 적용
    private lateinit var binding: DialogWarningBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogWarningBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners(){
        binding.btnYes.setOnClickListener {
            this.dismiss()
            yesClickListener.invoke()
        }
        binding.btnNo.onSingleClick() {
            dismiss()
        }
    }

    companion object {
        // fragment이므로 이런식으로 넘겨주어야함

        private lateinit var yesClickListener: () -> Unit

        fun newInstance(yesClickListener: ()-> Unit): DialogSaveWarning {
            val fragment = DialogSaveWarning()
            this.yesClickListener = yesClickListener
            return fragment
        }
    }


}

