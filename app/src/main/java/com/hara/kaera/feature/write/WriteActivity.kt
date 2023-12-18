package com.hara.kaera.feature.write

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.DetailBeforeActivity
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.getDeadline
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.custom.DialogBackpressWarning
import com.hara.kaera.feature.write.custom.DialogSaveWarning
import com.hara.kaera.feature.write.custom.DialogWriteComplete
import com.hara.kaera.feature.write.custom.TemplateChoiceBottomSheet
import com.hara.kaera.feature.write.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

/*
 * 진입 flow (구분 기준 : intent 내 StringExtra('action')
 * 1-1) [작성] 홈화면 -> 원석/보석 클릭
 * 1-2) [작성] 보관함 -> 좌상단 -> 템플릿 눌러서 '작성하러 가기'
 * 2) 수정 : DetailBeforeActivity -> WriteActivity(글 수정 중) -> DetailBeforeActivity
*/

// 진입 시점 구분 기준 : viewModel 내 worryId > 0

// TODO: 전반적으로 shortInfo -> guideline으로 네이밍 바꿔야한다

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeNote: EditText
    private lateinit var edittextTitle: EditText

    private var titleCondition = false
    private var contentCondition = false

    private lateinit var writeWorryReqDTO: WriteWorryReqDTO

    private val viewModel by viewModels<WriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkBackPressWarning()
                }
            }
        )

        editTextList = listOf<EditText>(
            binding.clTemplate.etAnswer1,
            binding.clTemplate.etAnswer2,
            binding.clTemplate.etAnswer3,
            binding.clTemplate.etAnswer4
        )
        editTextFreeNote = binding.clFreenote.etFreenote
        edittextTitle = binding.etTitle

        getDetailToEditData() // DetailBeforeActivity -> WriteActivity 넘어온 것인지 확인
        setTextWatcher()
        setClickListeners()
        collectFlows()
        setTemplate()
    }

    private fun setTemplate() {
        if (intent.hasExtra("templateId")) {
            viewModel.setTemplateId(intent.getIntExtra("templateId", -1))
        }
    }

    private fun setClickListeners() {
        binding.apply {
            appbarDetail.setNavigationOnClickListener {
                checkBackPressWarning()
            }
            clChoice.onSingleClick {
                if (checkText()) { // 한 글자라도 써놨을 경우
                    DialogSaveWarning { showTemplateChoiceBottomSheet() }.show(
                        supportFragmentManager,
                        "warning"
                    )
                } else {
                    showTemplateChoiceBottomSheet()
                }
            }

            btnComplete.onSingleClick {
                if (!titleCondition) KaeraSnackBar.make(
                    binding.root, baseContext.stringOf(R.string.write_snackbar_title),
                    KaeraSnackBar.DURATION.SHORT
                ).show()
                else if (!contentCondition) KaeraSnackBar.make(
                    binding.root,
                    baseContext.stringOf(R.string.write_snackbar_content),
                    KaeraSnackBar.DURATION.SHORT
                ).show()
                else {
                    // 1) 글 작성
                    if (viewModel.getWorryId() <= 0) {
                        DialogWriteComplete(
                            fun(day: Int) {
                                // Timber.e("[ABCD] 가져온 value는 ${day}")

                                // 1-1) free flow
                                if (viewModel.templateIdFlow.value == Constant.freeNoteId) {
                                    writeWorryReqDTO = WriteWorryReqDTO(
                                        templateId = Constant.freeNoteId,
                                        title = binding.etTitle.text.toString(),
                                        answers = listOf(
                                            binding.clFreenote.etFreenote.text.toString()
                                        ),
                                        deadline = day
                                    )
                                }
                                // 1-2) 나머지 template
                                else {
                                    writeWorryReqDTO = WriteWorryReqDTO(
                                        templateId = viewModel.templateIdFlow.value,
                                        title = binding.etTitle.text.toString(),
                                        answers = listOf(
                                            binding.clTemplate.etAnswer1.text.toString(),
                                            binding.clTemplate.etAnswer2.text.toString(),
                                            binding.clTemplate.etAnswer3.text.toString(),
                                            binding.clTemplate.etAnswer4.text.toString(),
                                        ),
                                        deadline = day
                                    )
                                }
                                viewModel.writeWorry(writeWorryReqDTO)
                            }
                        ).show(supportFragmentManager, "complete")
                    }
                    // 2) 글 수정
                    else {
                        var editWorryReqDTO = EditWorryReqDTO(
                            worryId = viewModel.getWorryId(),
                            templateId = viewModel.templateIdFlow.value,
                            title = binding.etTitle.text.toString(),
                            answers =
                            if (viewModel.templateIdFlow.value == Constant.freeNoteId) // free flow
                                listOf(binding.clFreenote.etFreenote.text.toString())
                            else
                                listOf(
                                    binding.clTemplate.etAnswer1.text.toString(),
                                    binding.clTemplate.etAnswer2.text.toString(),
                                    binding.clTemplate.etAnswer3.text.toString(),
                                    binding.clTemplate.etAnswer4.text.toString(),
                                )
                        )
                        viewModel.editWorry(editWorryReqDTO)
                    }
                }
            }
            layoutError.layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getTemplateDetailData()
            }
            layoutError.layoutInternalError.btnInternalError.onSingleClick {
                viewModel.getTemplateDetailData()
            }
        }
    }

    private fun setTextWatcher() {
        edittextTitle.addTextChangedListener {
            binding.tvTitleCount.text =
                String.format(this.stringOf(R.string.write_title_count), it!!.length)
            if (viewModel.templateIdFlow.value == Constant.freeNoteId) checkFreeFlow()
            else checkTemplate()
        }
        editTextFreeNote.addTextChangedListener {
            checkFreeFlow()
        }
        editTextList.forEach {
            it.addTextChangedListener { checkTemplate() }
        }
        editTextList[3].addTextChangedListener {
            binding.clTemplate.tvThanks.text =
                String.format(this.stringOf(R.string.write_thanksto), it)
        }
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.templateIdFlow.collect {
                        if (it in 1..6 && it != viewModel.curTemplateIdFlow.value) {
                            viewModel.getTemplateDetailData()
                        }
                    }
                }
                launch {
                    viewModel.templateDetailFlow.collect {
                        if (viewModel.templateIdFlow.value != viewModel.curTemplateIdFlow.value)
                            render(it)
                    }
                }
                launch {
                    viewModel.writeWorryFlow.collect {
                        Timber.e("[ABC] 글작성 $it")
                        if (it is UiState.Success) {
                            startActivity(
                                Intent(applicationContext, DetailBeforeActivity::class.java).apply {
                                    putExtra("worryId", 409) // TODO: 예린언니가 API 수정해주면 바꿀 예정
                                    putExtra("action", "write")
                                }
                            )
                            finish()
                        }
                    }
                }
                launch {
                    viewModel.editWorryFlow.collect {
                        Timber.e("[ABC] 글수정 $it")
                        if (it is UiState.Success) {
                            startActivity(
                                Intent(applicationContext, DetailBeforeActivity::class.java).apply {
                                    putExtra("worryId", viewModel.getWorryId())
                                    putExtra("action", "edit")
                                }
                            )
                            finish()
                        }
                    }
                }
                launch {
                    viewModel.detailStateFlow.collect {
                        renderForEdit(it)
                    }
                }
            }
        }
        // 마찬가지로 코루틴 열고 수집을 하는데 생명주기에 맞춰서 flow가 자동으로
        // 꺼지고 수집하고 될수 있도록 repeatOnLifeCycle이란걸 사용! 그리고
        // 뷰모델에 있는 StateFlow에서 뷰모델에서 해줬던것처럼 collect 해준다/
        // 여기 내부 값은 UiState가 들어오게 된다!
    }

    // [글 수정] EditText에 글 detail 내용 value를 넣는다
    private fun renderForEdit(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO: 이건 뭐지
            is UiState.Loading -> binding.layoutLoading.root.visible(true)
            is UiState.Success<WorryDetailEntity> -> {
                viewModel.setTemplateId(uiState.data.templateId)
                binding.userInput = uiState.data
            }
            is UiState.Error -> {
                binding.root.makeToast(uiState.error)
            }
        }
    }

    // templateId에 따라 전반적인 구조 visibility 조절
    // 실제로 뷰에서 대응하는 함수 프로그래스바 visibility 조절, error msg 출력 등을 하면 된다!
    private fun render(uiState: UiState<TemplateDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO: 추가해주세요 (written by. 수현)
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                renderTemplate(true)
                binding.templatedata = uiState.data
                if (viewModel.templateIdFlow.value == Constant.freeNoteId) { // free flow
                    binding.clTemplate.root.visible(false)
                    binding.clFreenote.root.visible(true)
                } else if (viewModel.templateIdFlow.value in 2..6) { // 일반 template
                    binding.clFreenote.root.visible(false)
                    binding.clTemplate.root.visible(true)
                    if (viewModel.templateIdFlow.value == Constant.thanksToId)
                        binding.clTemplate.tvThanks.visible(true) // thanksTo 템플릿
                    else binding.clTemplate.tvThanks.visible(false)
                }
            }

            is UiState.Error -> {
                renderTemplate(false)
                when (uiState.error) {
                    Constant.networkError -> {
                        binding.layoutError.layoutNetworkError.root.visible(true)
                    }

                    Constant.internalError -> {
                        binding.layoutError.layoutInternalError.root.visible(true)
                    }
                }
                binding.root.makeToast(uiState.error)
            }
        }
    }

    private fun renderTemplate(success: Boolean) {
        binding.layoutLoading.root.visible(false)
        binding.clEmpty.root.visible(false)
        binding.scrollView.visible(success)
        binding.layoutError.root.visible(!success)
    }

    private fun clearEditText() {
        editTextList.forEach {
            it.text.clear()
        }
        editTextFreeNote.text.clear()
        edittextTitle.text.clear()
        //TODO condtion 변수들 초기화
    }

    private fun checkFreeFlow() {
        contentCondition = editTextFreeNote.text.isNotEmpty() && editTextFreeNote.text.isNotBlank()
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }

    private fun checkTemplate() {
        contentCondition = editTextList.all { it.text.isNotEmpty() && it.text.isNotBlank() }
        titleCondition = edittextTitle.text.isNotEmpty() && edittextTitle.text.isNotBlank()
        binding.activate = (titleCondition && contentCondition)
    }

    private fun checkText(): Boolean =
        (titleCondition) || (editTextList.any { it.text.isNotEmpty() && it.text.isNotBlank() }) || (editTextFreeNote.text.isNotEmpty() || editTextFreeNote.text.isNotEmpty() && editTextFreeNote.text.isNotBlank())

    private fun getDetailToEditData() {

        when (intent.getStringExtra("action")) {
            "write" -> { // 글 작성
                showTemplateChoiceBottomSheet() // activity 진입하자마자 bottom sheet 등장
            }
            "edit" -> { // 글 수정
                viewModel.setWorryId(intent.getIntExtra("worryId", 0))
                viewModel.getWorryDetail()
            }
        }
    }

    private fun showTemplateChoiceBottomSheet() {
        TemplateChoiceBottomSheet({ id, shortInfo ->
            viewModel.setTemplateData(id, shortInfo)
        }, viewModel.templateIdFlow.value).show(
            supportFragmentManager,
            "template_choice"
        )
    }

    private fun checkBackPressWarning() {
        if (checkText()) {
            DialogBackpressWarning {
                finish()
            }.show(supportFragmentManager, "backpress_warning")
        } else {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setCurTemplateId(viewModel.templateIdFlow.value)
        // 백그라운드 전환시 render 재실행으로 인한 텍스트 초기화를 막기 위해 flow에 현재 id 저장
    }

}