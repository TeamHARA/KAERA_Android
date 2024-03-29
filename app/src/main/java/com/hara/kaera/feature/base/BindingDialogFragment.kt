package com.hara.kaera.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.hara.kaera.R
import com.hara.kaera.feature.util.dpToPx

abstract class BindingDialogFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int, private val marginHorizontal: Int
) : DialogFragment() {

    private var _binding: T? = null
    val binding get() = requireNotNull(_binding) { "${this.id} binding error" }

    override fun getTheme(): Int = R.style.NoMarginsDialog
    override fun onStart() {
        super.onStart()
        setupWidthToMatchParent()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        _binding!!.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 마진 역시 Match_parent가 임의로 적용된 이후에 다시 재 설정해주어야 합니다.
        (binding.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
            marginEnd = marginHorizontal.dpToPx(binding.root.context)
            marginStart = marginHorizontal.dpToPx(binding.root.context)
        }

    }

    private fun setupWidthToMatchParent() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}