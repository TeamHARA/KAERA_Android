package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentWorryMiningBinding
import com.hara.kaera.feature.base.BindingDialogFragment
import com.hara.kaera.feature.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DialogMineFragment(
    private val onClickComplete: (Editable) -> Unit,
) : BindingDialogFragment<DialogFragmentWorryMiningBinding>(R.layout.dialog_fragment_worry_mining, 20) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 완료 버튼 클릭
        binding.btnComplete.onSingleClick {
            onClickComplete(binding.etComplete.text)
            Timber.e("[ABC] DialogMineFragment - 완료 버튼 클릭")
            dismiss()
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.etComplete.addTextChangedListener {
            binding.btnComplete.isEnabled = it!!.isNotBlank() && it!!.isNotEmpty()
        }

    }

}