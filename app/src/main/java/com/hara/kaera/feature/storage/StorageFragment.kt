package com.hara.kaera.feature.storage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentStorageBinding
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.feature.base.BindingFragment
import com.hara.kaera.feature.detail.DetailAfterActivity
import com.hara.kaera.feature.home.HomeFragment
import com.hara.kaera.feature.mypage.MypageActivity
import com.hara.kaera.feature.storage.adapter.StorageGridAdapter
import com.hara.kaera.feature.storage.viewmodel.StorageViewModel
import com.hara.kaera.feature.storage.worrytemplate.WorryTemplateActivity
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.StorageTemplateChoiceBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StorageFragment : BindingFragment<FragmentStorageBinding>(R.layout.fragment_storage) {

    private val viewModel by viewModels<StorageViewModel>()
    private lateinit var storageAdapter: StorageGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        setClickListeners()
        collectFlows()
    }

    override fun onResume() {
        super.onResume()
        initLayout()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.worryStateFlow.collect { uiState ->
                        render(uiState)
                    }
                }
                launch {
                    viewModel.templateId.collect { _ ->
                        viewModel.getJewels()
                    }
                }
            }
        }
    }

    private fun render(uiState: UiState<WorryByTemplateEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.loadingBar.root.visible(true)

            is UiState.Success<WorryByTemplateEntity> -> {
                controlLayout(success = true)
                val worryByTemplate = uiState.data
                storageAdapter.submitList(worryByTemplate.worryList)
                binding.total = worryByTemplate.totalNum
            }

            is UiState.Error -> {
                controlLayout(success = false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    binding.root
                )
            }

            is UiState.Empty -> {
                controlLayout(success = true, empty = true)
            }
        }
    }

    private fun initLayout() {
        storageAdapter = StorageGridAdapter { worryId ->
            startActivity(
                Intent(context, DetailAfterActivity::class.java).apply {
                    putExtra(Constant.worryIdIntent, worryId)
                },
            )
        }
        binding.rvJewels.adapter = storageAdapter
        viewModel.getJewels()
    }

    private fun setClickListeners() {
        binding.apply {
            clChoice.setOnClickListener {
                StorageTemplateChoiceBottomSheet({ templateId, title ->
                    viewModel.setSelectedId(templateId)
                    tvTemplateTitle.text = title
                }, viewModel.templateId.value, {
                    if (viewModel.templateId.value != viewModel.selectedId.value) { // onDismissed
                        viewModel.setTemplateId()
                    }
                }).show(parentFragmentManager, "template_choice")
            }

            with(btnHelp) {
                increaseTouchSize(requireContext())
                setOnClickListener {
                    startActivity(Intent(context, WorryTemplateActivity::class.java))
                }
            }

            btnMypage.onSingleClick {
                startActivity(Intent(context, MypageActivity::class.java))
            }

            clEmpty.btnGoMining.onSingleClick {
                val bottomNavigationView =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
                bottomNavigationView.selectedItemId = R.id.nav_home

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.cl_fragment_container, HomeFragment())
                    .commit()
            }

            with(layoutError) {
                layoutNetworkError.btnNetworkError.onSingleClick {
                    viewModel.getJewels()
                }
                layoutInternalError.btnInternalError.onSingleClick {
                    viewModel.getJewels()
                }
            }
        }
    }

    private fun controlLayout(success: Boolean, empty: Boolean = false) {
        binding.loadingBar.root.visible(false)
        binding.clEmpty.root.visible(success && empty)
        binding.clContent.visible(success && !empty)
        binding.layoutError.root.visible(!success)
    }

}
