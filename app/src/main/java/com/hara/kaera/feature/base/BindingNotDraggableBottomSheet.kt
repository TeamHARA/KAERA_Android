package com.hara.kaera.feature.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BindingNotDraggableBottomSheet<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    BottomSheetDialogFragment() {
    // 드래그해서 닫는게 불가능해야 하는 바텀시트
    private var _binding: T? = null
    val binding get() = requireNotNull(_binding) { "${androidx.appcompat.R.id.content} binding error" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        _binding!!.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val behavior = bottomSheetDialog.behavior
            behavior.apply {
                isDraggable = false // 드래그 불가
                skipCollapsed = true
                disableShapeAnimations()
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return bottomSheetDialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}