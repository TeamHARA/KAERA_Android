package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentEditBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogEditFragment :
    BindingDialogFragment<DialogFragmentEditBinding>(R.layout.dialog_fragment_edit, 26) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    // TODO: touch target 더 크게 해야 하나?
    private fun setClickListener() {
        with(binding) {
            tvEdit.setOnClickListener { // 수정하기

            }
            tvDeadlineEdit.setOnClickListener { // 데드라인 수정하기

            }
            tvDelete.setOnClickListener { // 삭제하기

            }
            tvCancel.setOnClickListener { // 취소
                val fragmentManager = activity?.getSupportFragmentManager()
                fragmentManager?.beginTransaction()?.remove(this@DialogEditFragment)?.commit()
                fragmentManager?.popBackStack()
            }
        }
    }
}