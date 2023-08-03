package com.hara.kaera.presentation.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hara.kaera.R

abstract class BindingDraggableBottomSheet<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    BottomSheetDialogFragment() {
    // 드래그해서 닫을 수 있는 클래스

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    open fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        bottomSheetDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val behavior = bottomSheetDialog.behavior
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.apply {
//            state = BottomSheetBehavior.STATE_COLLAPSED
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true // 바텀 시트를 접을때 절반에서 멈추는 경우를 방지하고 한번에 쭉 내려감
            // 일단 남겨둠
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
            isHideable = true
        }
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 75 / 100
        // 기기 높이 대비 비율 설정 부분!!
        // 위 수치는 기기 높이 대비 75%로 다이얼로그 높이를 설정
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}