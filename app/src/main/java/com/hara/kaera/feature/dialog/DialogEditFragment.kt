package com.hara.kaera.feature.dialog

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogFragmentEditBinding
import com.hara.kaera.feature.base.BindingDialogFragment

class DialogEditFragment(
    private val onClickEdit: () -> Unit,
    private val onClickEditDeadline: () -> Unit,
    private val onClickDelete: () -> Unit
) : BindingDialogFragment<DialogFragmentEditBinding>(R.layout.dialog_fragment_edit, 26) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        with(binding) {
            tvEdit.setOnClickListener { // 수정하기
                onClickEdit()
                dismiss()
            }
            tvDeadlineEdit.setOnClickListener { // 데드라인 수정하기
                onClickEditDeadline()
                dismiss()
            }
            tvDelete.setOnClickListener { // 삭제하기
                dismiss()
                onClickDelete.invoke()
            }
            tvCancel.setOnClickListener { // 취소
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.beginTransaction()?.remove(this@DialogEditFragment)?.commit()
                fragmentManager?.popBackStack()
            }
        }
    }
}