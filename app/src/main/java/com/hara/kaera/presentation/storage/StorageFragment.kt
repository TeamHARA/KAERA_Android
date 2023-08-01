package com.hara.kaera.presentation.storage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentStorageBinding
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.util.onSingleClick
import com.hara.kaera.presentation.write.Mode
import com.hara.kaera.presentation.write.TemplateChoiceBottomSheet
import timber.log.Timber

class StorageFragment :
    BindingFragment<FragmentStorageBinding>(R.layout.fragment_storage) {

    private val viewModel by viewModels<StorageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            clChoice.onSingleClick(1000) {
                TemplateChoiceBottomSheet({
                    viewModel.setTemplateId(it)
                    Timber.d(viewModel.templateId.value.toString())
                }, Mode.STORAGE).show(parentFragmentManager, "template_choice")
            }
        }
    }
}
