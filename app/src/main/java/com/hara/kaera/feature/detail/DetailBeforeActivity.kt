package com.hara.kaera.feature.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.databinding.ActivityDetailBeforeBinding
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.EditDeadlineEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.custom.DialogDeleteWarning
import com.hara.kaera.feature.dialog.DialogEditFragment
import com.hara.kaera.feature.dialog.DialogMineFragment
import com.hara.kaera.feature.dialog.DialogSayingFragment
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.WriteActivity
import com.hara.kaera.feature.write.custom.DialogWriteComplete
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailBeforeActivity :
    BindingActivity<ActivityDetailBeforeBinding>(R.layout.activity_detail_before) {
    private val viewModel by viewModels<DetailBeforeViewModel>()
    private val homeVm by viewModels<HomeViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWorryById()
        collectFlows()
        setClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getWorryById() {
        intent.getParcelableExtra("worryDetail", WorryDetailEntity::class.java)
            ?.let { intentWorryDetail ->
                binding.worryDetail = intentWorryDetail

                val action = intent.getStringExtra("action")
                if (action == "view") { // view
                    viewModel.getWorryDetail(binding.worryDetail!!.worryId)
                    return
                }

                // write result | edit result
                KaeraSnackBar.make(
                    view = binding.root,
                    message = if (action == "write") baseContext.getString(R.string.complete_write_snackbar)
                    else baseContext.getString(R.string.complete_edit_snackbar),
                    duration = KaeraSnackBar.DURATION.SHORT,
                    backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                    locationY = Constant.completeSnackBarLocationY
                ).show()
            }
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.detailStateFlow.collect {
                        render(it)
                    }
                }
                launch {
                    viewModel.editDeadlineStateFlow.collect {
                        renderEditDeadline(it)
                    }
                }
                launch {
                    viewModel.deleteWorryFlow.collect {
                        renderDelete(it)
                    }
                }
                launch {
                    viewModel.decideFinalFlow.collect {
                        if (it is UiState.Success<String>) {
                            val sayingFragment = DialogSayingFragment(
                                templateId = viewModel.templateId,
                                saying = it.data
                            )
                            sayingFragment.show(supportFragmentManager, "saying")
                            Handler(Looper.getMainLooper()).postDelayed({
                                sayingFragment.dismiss()
                                finish()
                                startActivity(
                                    Intent(baseContext, DetailAfterActivity::class.java).apply {
                                        putExtra(
                                            Constant.worryIdIntent,
                                            binding.worryDetail!!.worryId
                                        )
                                    },
                                )
                            }, 2000) // 애니메이션 시작전 2000ms 후 자동종료

                        }
                    }
                }
            }
        }
    }

    // view
    private fun render(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO
            is UiState.Loading -> binding.layoutLoading.root.visible(true)
            is UiState.Success<WorryDetailEntity> -> {
                uiState.data.worryId = binding.worryDetail!!.worryId
                binding.worryDetail = uiState.data
                Timber.e("[ABC] 서버 통신으로 가져온 worryDetail ${binding.worryDetail}")

                controlLayout(true)
            }

            is UiState.Error -> {
                controlLayout(false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    root = binding.root
                )
            }
        }
    }

    private fun controlLayout(success: Boolean) {
        binding.layoutLoading.root.visible(false)
        binding.btnSubmit.visible(success)
        binding.svContent.visible(success)
        binding.layoutError.root.visible(!success)
    }

    // 데드라인 수정
    private fun renderEditDeadline(uiState: UiState<EditDeadlineEntity>) {
        when (uiState) {
            is UiState.Success<EditDeadlineEntity> -> {
                with(binding.worryDetail!!) {
                    deadline = uiState.data.deadline
                    dDay = uiState.data.dDay
                } // bidning.worryDetail을 갈아끼는 게 아니기에 'd-?' text는 따로 바꿔줘야 한다(UI)
                binding.tvAppbarDay.text = when (uiState.data.dDay) {
                    Constant.infiniteDeadLine -> {
                        "D-∞"
                    }

                    else -> "D" + uiState.data.dDay
                }

                KaeraSnackBar.make(
                    view = binding.root,
                    message = "데드라인 수정 완료!",
                    duration = KaeraSnackBar.DURATION.SHORT,
                    backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                    locationY = Constant.completeSnackBarLocationY
                ).show()
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }

            else -> Unit
        }
    }

    private fun renderDelete(uiState: UiState<DeleteWorryEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)
            is UiState.Success -> {
                finish()
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun setClickListener() {
        with(binding) {
            with(layoutError) {
                layoutInternalError.btnInternalError.onSingleClick {
                    viewModel.getWorryDetail(binding.worryDetail!!.worryId)
                }
                layoutNetworkError.btnNetworkError.onSingleClick {
                    viewModel.getWorryDetail(binding.worryDetail!!.worryId)
                }
            }
            with(btnClose) {
                increaseTouchSize(baseContext)
                setOnClickListener { // 앱바 'X' 버튼 클릭
                    homeVm.getHomeWorryList(false)
                    finish()
                }
            }
            with(btnEdit) {
                increaseTouchSize(baseContext)
                setOnClickListener {// 햄버거 버튼
                    DialogEditFragment(
                        // 1) [글 수정] WriteActivity로 이동
                        {
                            startActivity(
                                Intent(baseContext, WriteActivity::class.java).apply {
                                    putExtra("action", "edit")
                                    putExtra("worryDetail", binding.worryDetail)
                                }
                            )
                            finish()
                        },
                        // 2) [데드라인 수정]
                        {
                            DialogWriteComplete(
                                fun(day: Int) {
                                    viewModel.editDeadline(binding.worryDetail!!.worryId, day)
                                }, "edit", binding.worryDetail!!.dDay
                            ).show(supportFragmentManager, "complete")
                        },
                        // 3) [삭제]
                        {
                            DialogDeleteWarning {
                                viewModel.deleteWorry(binding.worryDetail!!.worryId)
                            }.show(supportFragmentManager, "delete")
                        }
                    ).show(supportFragmentManager, "edit")
                }
            }
            btnSubmit.setOnClickListener { // 하단 "고민 보석 캐기" 버튼
                DialogMineFragment(
                    fun(finalAnswer: Editable) {
                        val decideFinalReqDTO = DecideFinalReqDTO(
                            worryId = binding.worryDetail!!.worryId,
                            finalAnswer = finalAnswer.toString()
                        )
                        Timber.e("[ABC] 최종 결정 하러 가자 - $decideFinalReqDTO")
                        viewModel.decideFinal(decideFinalReqDTO)
                    }
                ).show(supportFragmentManager, "mine")
            }
        }
    }

}