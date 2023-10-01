package com.hara.kaera.feature.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.databinding.ActivityDetailBeforeBinding
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.custom.DialogDeleteWarning
import com.hara.kaera.feature.dialog.DialogEditFragment
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.write.WriteActivity
import com.hara.kaera.feature.write.custom.DialogWriteComplete
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber


@AndroidEntryPoint
class DetailBeforeActivity :
    BindingActivity<ActivityDetailBeforeBinding>(R.layout.activity_detail_before) {
    private val viewModel by viewModels<DetailBeforeViewModel>()
    private val homeVm by viewModels<HomeViewModel>()

    private var editDayCount = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWorryById()
        collectFlows()
        setClickListener()
    }

    private fun getWorryById() {
        val bundle = intent.extras
        if (bundle != null) {
            val json =
                bundle.getString("worryDetailEntity") // Retrieve the JSON string from the Bundle

            // 1) WriteActivity(글작성) -> DetailBeforeActivity
            if (json != null) {
                // Convert the JSON string back to a WriteWorryReqDTO object
                val worryDetail = Json.decodeFromString<WorryDetailEntity>(json)
                renderData(worryDetail)
                // TODO: 이렇게 서버 통신 안 할 거면, 뒤로 가기 == 홈화면 일 때 서버 통신 다시 해서(?) 추가된 보석이 반영돼야 하는데..
                // TODO: 정말 로그 찍어보니 서버 통신을 다시 안 한다! MainActivity 내 HomeFragment 내 HomeStoneFragment가 살아있어서 그런 듯..
                // TODO: 이 activity에서 뒤로가기 누르면 서버 통신 다시 하도록 해야 하나..

                KaeraSnackBar.make(
                    binding.root, "작성 완료!",
                    KaeraSnackBar.DURATION.LONG
                ).show() // TODO: 서버에 잘 날아갔으면 (customized) ToastMessage 띄우기
                return
            }

            // 2-1) HomeStoneFragment -> DetailBeforeActivity
            // 2-2) DetailBeforeActivity -> WriteActivity(글수정 중) -> DetailBeforeActivity
            val worryId = intent.getIntExtra("worryId", 0)
            viewModel.getWorryDetail(worryId)

            // 2-2
            val from = intent.getStringExtra("from")
            if ("edit" == from) {
                KaeraSnackBar.make(
                    binding.root, "수정 완료!",
                    KaeraSnackBar.DURATION.LONG
                ).show()
            }
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
                        binding.tvAppbarDay.text = "D+$editDayCount"
                        KaeraSnackBar.make(
                            binding.root, "데드라인 수정 완료!",
                            KaeraSnackBar.DURATION.LONG
                        ).show()
                    }
                }
                launch {
                    viewModel.deleteWorryFlow.collect {
                        renderDelete(it)
                    }
                }
            }
        }
    }

    private fun render(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> Unit
            is UiState.Success<WorryDetailEntity> -> {
                renderData(uiState.data)
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun renderDelete(uiState: UiState<DeleteWorryEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> Unit
            is UiState.Success -> {
                Timber.e("[ABC] 삭제 되었습니다!")
                finish()
            }

            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
        }
    }

    private fun renderData(worryDetail: WorryDetailEntity) {
        binding.worryDetail = worryDetail

        if (worryDetail.templateId == 1) { // free flow
            binding.cvContent.visibility = View.GONE
            binding.frCvContent.visibility = View.VISIBLE
        } else {
            binding.cvContent.visibility = View.VISIBLE
            binding.frCvContent.visibility = View.GONE
        }
    }

    private fun setClickListener() {
        with(binding) { // 앱바 'X' 버튼 클릭
            appbarDetail.setNavigationOnClickListener {
                Timber.e("[ABC] DetailBeforeActivity에서 x버튼 눌렀사와요")
                homeVm.getHomeWorryList(false)
                finish()
            }
            btnEdit.setOnClickListener {// 햄버거 버튼
                DialogEditFragment(
                    // 1) 수정하기 -> activity_write으로 이동(시 데이터 전달)
                    { goToWriteActivity() },
                    // 2) 데드라인 수정하기
                    { DialogWriteComplete(
                        fun (day: Int) {
                            editDayCount = day
                            val editDeadlineReqDTO = EditDeadlineReqDTO(
                                worryId = viewModel.detailToEditData.worryId,
                                dayCount = day
                            )
                            Timber.e("[ABC] 데드라인 수정하기: $editDeadlineReqDTO")
                            viewModel.editDeadline(editDeadlineReqDTO)
                        }
                    ).show(supportFragmentManager, "complete") },
                    // 3) 삭제하기
                    { DialogDeleteWarning {
                            viewModel.deleteWorry()
                        }.show(supportFragmentManager, "delete") }
                ).show(supportFragmentManager, "edit")
            }
            btnSubmit.setOnClickListener { // 하단 "고민 보석 캐기" 버튼

            }
        }
    }

    // 수정하기 -> activity_write으로 이동 시 데이터 전달
    private fun goToWriteActivity() {
        val json = Json.encodeToString(viewModel.detailToEditData)
        var bundle = Bundle()
        bundle.putString("detailToEditData", json)
        bundle.putInt("templateId", viewModel.templateId)
        val intent = Intent(this, WriteActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}