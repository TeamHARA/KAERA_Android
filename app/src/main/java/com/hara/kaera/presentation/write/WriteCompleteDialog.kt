package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hara.kaera.R
import com.hara.kaera.databinding.DialogCompleteBinding
import com.hara.kaera.presentation.util.dpToPx

class WriteCompleteDialog() : DialogFragment() {

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
            marginEnd = 16.dpToPx(binding.root.context)
            marginStart = 16.dpToPx(binding.root.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}