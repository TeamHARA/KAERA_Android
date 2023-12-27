package com.hara.kaera.feature.write

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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

// TODO: 전반적으로 shortInfo -> guideline으로 네이밍 바꿔야한다

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeNote: EditText
    private lateinit var edittextTitle: EditText

    private var titleCondition = false
    private var contentCondition = false

    private lateinit var action: String // write | edit
    private val viewModel by viewModels<WriteViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getDetailToEditData() {
        action = intent.getStringExtra("action")!!

        intent.getParcelableExtra("worryDetail", WorryDetailEntity::class.java)?.let { intentWorryDetail -> // 수정
            binding.userInput = intentWorryDetail
            viewModel.setTemplateId(intentWorryDetail.templateId)
        } ?: run { // 작성
            showTemplateChoiceBottomSheet() // activity 진입하자마자 bottom sheet 등장
        }
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
                if (!titleCondition) {
                    KaeraSnackBar.make(
                        binding.root, baseContext.stringOf(R.string.write_snackbar_title),
                        KaeraSnackBar.DURATION.SHORT
                    ).show()
                    return@onSingleClick
                }
                if (!contentCondition) {
                    KaeraSnackBar.make(
                        binding.root,
                        baseContext.stringOf(R.string.write_snackbar_content),
                        KaeraSnackBar.DURATION.SHORT
                    ).show()
                    return@onSingleClick
                }

                when(action) {
                    "write" -> { // [글 작성]
                        DialogWriteComplete(
                            fun(day: Int) {
                                viewModel.writeWorry(
                                    title = binding.etTitle.text.toString(),
                                    answers = getAnswers(),
                                    dDay = day
                                )
                            }, "write", -1
                        ).show(supportFragmentManager, "complete")
                    }
                    "edit" -> { // [글 수정]
                        viewModel.editWorry(
                            worryId = binding.userInput!!.worryId,
                            title = binding.etTitle.text.toString(),
                            answers = getAnswers(),
                        )
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
                                    putExtra("action", "write")
                                    it.data.subtitles = binding.templatedata!!.questions // [고민작성 API - response]에 subtitles는 안 넘어와서..
                                    putExtra("worryDetail", it.data)
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
                                    putExtra("action", "edit")
                                    with(binding.userInput!!) {
                                        // TODO: template 변경 가능하면 templateId, subtitles 등도 바뀌어야..?
                                        title = binding.etTitle.text.toString()
                                        answers = getAnswers()
                                    }
                                    putExtra("worryDetail", binding.userInput)
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

    private fun getAnswers(): List<String> {
        return if (viewModel.templateIdFlow.value == Constant.freeNoteId)
                listOf(binding.clFreenote.etFreenote.text.toString())
            else
                listOf(
                    binding.clTemplate.etAnswer1.text.toString(),
                    binding.clTemplate.etAnswer2.text.toString(),
                    binding.clTemplate.etAnswer3.text.toString(),
                    binding.clTemplate.etAnswer4.text.toString(),
                )
    }

    override fun onStop() {
        super.onStop()
        viewModel.setCurTemplateId(viewModel.templateIdFlow.value)
        // 백그라운드 전환시 render 재실행으로 인한 텍스트 초기화를 막기 위해 flow에 현재 id 저장
    }

}