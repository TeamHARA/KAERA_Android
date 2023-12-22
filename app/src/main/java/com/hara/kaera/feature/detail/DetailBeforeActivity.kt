package com.hara.kaera.feature.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditDeadlineResDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.databinding.ActivityDetailBeforeBinding
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.custom.DialogDeleteWarning
import com.hara.kaera.feature.dialog.DialogEditFragment
import com.hara.kaera.feature.dialog.DialogMineFragment
import com.hara.kaera.feature.home.HomeViewModel
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.WriteActivity
import com.hara.kaera.feature.write.custom.DialogWriteComplete
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

// [23.12.17] DetailBeforeActivity가 create 되면
// 일단 무조건, intent로 온 worryId를 바탕으로 고민의 detail을 가져오는 서버통신 수행한다.

/*
 * 진입 flow (구분 기준 : intent 내 StringExtra('action'))
 * 1) 작성 : WriteActivity -> DetailBeforeActivity
 * 2) 조회 : HomeStoneFragment -> DetailBeforeActivity
 * 3) 수정 : DetailBeforeActivity -> WriteActivity(글 수정 중) -> DetailBeforeActivity
*/

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
        viewModel.setWorryId(intent.getIntExtra("worryId", -1))

        intent.getParcelableExtra("worryDetail", WorryDetailEntity::class.java)?.let { intentWorryDetail -> // 1, 2) 작성, 수정
            binding.worryDetail = intentWorryDetail

            val action = intent.getStringExtra("action")
            val message = if (action == "write") { // 작성 후 결과
                baseContext.getString(R.string.complete_write_snackbar)
            } else { // 수정 후 결과
                baseContext.getString(R.string.complete_edit_snackbar)
            }

            KaeraSnackBar.make(
                view = binding.root,
                message = message,
                duration = KaeraSnackBar.DURATION.SHORT,
                backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                locationY = Constant.completeSnackBarLocationY
            ).show()
        } ?: run { // 3) 조회
            viewModel.getWorryDetail()
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
                        if (it is UiState.Success) { // UiState.init 때도 호출되면 안 돼
                            binding.worryDetail = createWorryDetail(it.data)

                            KaeraSnackBar.make(
                                view = binding.root,
                                message = "데드라인 수정 완료!",
                                duration = KaeraSnackBar.DURATION.SHORT,
                                backgroundColor = KaeraSnackBar.BACKGROUNDCOLOR.GRAY_5,
                                locationY = Constant.completeSnackBarLocationY
                            ).show()
                        }
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
                            Timber.e("[ABC] 최종 결정 성공!")
                            // TODO: quote 띄우기
                        }
                    }
                }
            }
        }
    }

    private fun render(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)
            is UiState.Success<WorryDetailEntity> -> {
                binding.layoutLoading.root.visible(false)
                renderData(uiState.data)
            }

            is UiState.Error -> {
                binding.layoutLoading.root.visible(false)
                binding.root.makeToast(uiState.error)
            }

            UiState.Empty -> TODO()
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

    private fun renderData(worryDetail: WorryDetailEntity) {
        binding.worryDetail = worryDetail

        binding.btnSubmit.visible(true)
        if (worryDetail.templateId == Constant.freeNoteId) { // free flow
            binding.cvContent.visible(false)
            binding.frCvContent.visible(true)
        } else {
            binding.cvContent.visible(true)
            binding.frCvContent.visible(false)
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
                    // 1) [글 수정] WriteActivity로 이동
                    {
                        startActivity(
                            Intent(applicationContext, WriteActivity::class.java).apply {
                                putExtra("worryId", viewModel.getWorryId())
                                putExtra("worryDetail", binding.worryDetail)
                                putExtra("action", "edit")
                            }
                        )
                        finish()
                    },
                    // 2) [데드라인 수정]
                    {
                        DialogWriteComplete(
                            fun(day: Int) {
                                viewModel.editDeadline(day)
                            }, "edit", binding.worryDetail!!.dDay
                        ).show(supportFragmentManager, "complete")
                    },
                    // 3) [삭제]
                    {
                        DialogDeleteWarning {
                            viewModel.deleteWorry()
                        }.show(supportFragmentManager, "delete")
                    }
                ).show(supportFragmentManager, "edit")
            }
            btnSubmit.setOnClickListener { // 하단 "고민 보석 캐기" 버튼
                DialogMineFragment(
                    fun(finalAnswer: Editable) {
                        val decideFinalReqDTO = DecideFinalReqDTO(
                            worryId = viewModel.getWorryId(),
                            finalAnswer = finalAnswer.toString()
                        )
                        Timber.e("[ABC] 최종 결정 하러 가자 - $decideFinalReqDTO")
                        viewModel.decideFinal(decideFinalReqDTO)
                    }
                ).show(supportFragmentManager, "mine")
            }
        }
    }

    private fun createWorryDetail(editDeadlineResDTO: EditDeadlineResDTO): WorryDetailEntity {
        return with(binding.worryDetail!!) {
            WorryDetailEntity(
                title = title,
                templateId = templateId,
                subtitles = subtitles,
                answers = answers,
                period = period,
                updatedAt = updatedAt,
                deadline = editDeadlineResDTO.data.deadline,
                dDay = editDeadlineResDTO.data.dDay,
                finalAnswer = finalAnswer,
                review = WorryDetailEntity.Review(
                    content = review.content,
                    updatedAt = review.updatedAt
                )
            )
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