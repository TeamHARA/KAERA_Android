package com.hara.kaera.presentation.storage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentStorageBinding
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.storage.adapter.StorageGridAdapter
import com.hara.kaera.presentation.storage.viewmodel.StorageViewModel
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.onSingleClick
import com.hara.kaera.presentation.write.StorageTemplateChoiceBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StorageFragment : BindingFragment<FragmentStorageBinding>(R.layout.fragment_storage) {

    private val viewModel by viewModels<StorageViewModel>()
    private lateinit var storageAdapter: StorageGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        setClickListeners()
        addObserve()
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.worryStateFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }
    }

    private fun render(uiState: UiState<WorryByTemplateEntity>) {
        when (uiState) {
            is UiState.Success<WorryByTemplateEntity> -> {
                val worryByTemplate = uiState.data.worryByTemplate!!
                if (!isEmpty(worryByTemplate.totalNum)) {
                    storageAdapter.submitList(worryByTemplate.worryList)
                }
                binding.tvSumJewelNum.text = String.format(
                    getString(
                        R.string.storage_jewels_num,
                        worryByTemplate.totalNum,
                    ),
                )
            }

            else -> {
                Timber.e("else")
            }
        }
    }

    private fun initLayout() {
        storageAdapter = StorageGridAdapter()
        binding.rvJewels.adapter = storageAdapter
        viewModel.getJewels()
    }

    private fun setClickListeners() {
        binding.apply {
            clChoice.onSingleClick(1000) {
                StorageTemplateChoiceBottomSheet({ // templateClickListener
                    viewModel.setSelectedId(it)
                    Timber.d(viewModel.selectedId.value.toString())
                }, viewModel.templateId.value ?: 0, {
                    if (viewModel.templateId.value != viewModel.selectedId.value) { // onDismissed
                        viewModel.setTemplateId()
                        Timber.d("****\nnew tempate id: ${viewModel.templateId.value}\n****")
                    }
                }).show(parentFragmentManager, "template_choice")
            }
        }
    }

    private fun addObserve() {
        viewModel.templateId.observe(viewLifecycleOwner) {
            viewModel.getJewels()
        }
    }

    private fun isEmpty(totalNum: Int): Boolean {
        return if (totalNum == 0) {
            binding.clEmpty.root.visibility = View.VISIBLE
            binding.tvSumJewelNum.visibility = View.GONE
            binding.rvJewels.visibility = View.GONE
            true
        } else {
            binding.clEmpty.root.visibility = View.GONE
            binding.tvSumJewelNum.visibility = View.VISIBLE
            binding.rvJewels.visibility = View.VISIBLE
            false
        }
    }
}
