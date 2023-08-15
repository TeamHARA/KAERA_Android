package com.hara.kaera.presentation.storage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentStorageBinding
import com.hara.kaera.presentation.base.BindingFragment
import com.hara.kaera.presentation.storage.adapter.StorageGridAdapter
import com.hara.kaera.presentation.storage.data.DummyStorageData
import com.hara.kaera.presentation.util.onSingleClick
import com.hara.kaera.presentation.write.StorageTemplateChoiceBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StorageFragment :
    BindingFragment<FragmentStorageBinding>(R.layout.fragment_storage) {

    private val viewModel by viewModels<StorageViewModel>()
    private lateinit var storageAdapter: StorageGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        setClickListeners()
        addObserve()
    }

    private fun initLayout() {
        storageAdapter = StorageGridAdapter()
        binding.rvJewels.adapter = storageAdapter
        storageAdapter.submitList(DummyStorageData.StorageData.worryList)
    }

    private fun setClickListeners() {
        binding.apply {
            clChoice.onSingleClick(1000) {
                StorageTemplateChoiceBottomSheet({
                    viewModel.setSelectedId(it)
                    Timber.d(viewModel.selectedId.value.toString())
                }, {
                    if (viewModel.templateId.value != viewModel.selectedId.value) { // 바텀시트 dismiss 시
                        viewModel.setTemplateId()
                        Timber.d("****\nnew tempate id: ${viewModel.templateId.value}\n****")
                    }
                }).show(parentFragmentManager, "template_choice")
            }
        }
    }

    private fun addObserve() {
        viewModel.templateId.observe(viewLifecycleOwner) {
            setTemplate()
        }
    }

    private fun setTemplate() {
        viewModel.getJewels()
        binding.tvSumJewelNum.text = String.format(
            getString(
                R.string.storage_jewels_num,
                DummyStorageData.StorageData.totalNum,
            ),
        )
        if (DummyStorageData.StorageData.totalNum == 0) {
            binding.clEmpty.root.visibility = View.VISIBLE
            binding.tvSumJewelNum.visibility = View.GONE
            binding.rvJewels.visibility = View.GONE
        } else {
            binding.clEmpty.root.visibility = View.GONE
            binding.tvSumJewelNum.visibility = View.VISIBLE
            binding.rvJewels.visibility = View.VISIBLE
        }
    }
}
