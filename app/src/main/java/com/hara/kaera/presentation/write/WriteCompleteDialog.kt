package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogCompleteBinding
import com.hara.kaera.presentation.util.dpToPx
import com.hara.kaera.presentation.util.onSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteCompleteDialog : DialogFragment() {

    private var _binding: DialogCompleteBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCompleteBinding.inflate(inflater, container, false)
        setupWidthToMatchParent()
        setNumberPicker()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 마진 역시 Match_parent가 임의로 적용된 이후에 다시 재 설정해주어야 합니다.
        (binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
            marginEnd = 16.dpToPx(binding.root.context)
            marginStart = 16.dpToPx(binding.root.context)
        }
    }


    private fun setOnClickListener() {
        binding.btnComplete.onSingleClick(1000) { }
        binding.btnNoDeadline.onSingleClick(1000) { }
    }


    private fun setNumberPicker() {
        binding.pickerDeadlines.apply {
            minValue = 1
            maxValue = 30
        }
    }

    /*
    다이얼로그들은 기본적으로 안드로이드에서 적용하는 속성때문에 Match_parent가
    xml에서만 주면 적용이 안됩니다!
     */
    private fun DialogFragment.setupWidthToMatchParent() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}