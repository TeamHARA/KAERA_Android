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

// bundle에서 가져온 detailToEditData가 null인지에 따라
// 1) [글작성] 글쓰기 위해 온 것인지
// 2) [글수정] DetailBeforeActivity에서 글 수정하러 온 것인지
// 구분한다!

@AndroidEntryPoint
class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var editTextList: List<EditText>
    private lateinit var editTextFreeNote: EditText
    private lateinit var edittextTitle: EditText

    private var titleCondition = false
    private var contentCondition = false

    // case 1에서만 사용
    private lateinit var writeWorryReqDTO: WriteWorryReqDTO

    // case 2에서만 사용
    private var templateId: Int = -1
    private var detailToEditData: EditWorryReqDTO? = null

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

        showTemplateChoiceBottomSheet() // 고민작성 진입하자마자 바텀시트가 나오게 되도록
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
                if (detailToEditData == null) { // 글쓰기 일 때만
                    if (checkText()) { // 한글자라도 써놨을 경우
                        DialogSaveWarning { showTemplateChoiceBottomSheet() }.show(
                            supportFragmentManager,
                            "warning"
                        )
                    } else {
                        showTemplateChoiceBottomSheet()
                    }
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
                    if (detailToEditData == null) { // 글 작성 서버 통신
                        DialogWriteComplete(
                            fun(day: Int) {
                                // Timber.e("[ABCD] 가져온 value는 ${day}")

                                if (viewModel.templateIdFlow.value == Constant.freeNoteId) { // free flow
                                    writeWorryReqDTO = WriteWorryReqDTO(
                                        templateId = Constant.freeNoteId,
                                        title = binding.etTitle.text.toString(),
                                        answers = listOf(
                                            binding.clFreenote.etFreenote.text.toString()
                                        ),
                                        deadline = day
                                    )
                                } else { // 나머지 template
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
                                Timber.e("글 작성 끝나서 서버통신 시작: $writeWorryReqDTO")
                                viewModel.writeWorry(writeWorryReqDTO)
                            }
                        ).show(supportFragmentManager, "complete")
                    } else { // 글 수정 서버 통신
                        var editWorryReqDTO = EditWorryReqDTO(
                            worryId = detailToEditData!!.worryId,
                            templateId = viewModel.templateIdFlow.value,
                            title = binding.etTitle.text.toString(),
                            answers =
                            if (templateId == Constant.freeNoteId) // free note
                                listOf(binding.clFreenote.etFreenote.text.toString())
                            else
                                listOf(
                                    binding.clTemplate.etAnswer1.text.toString(),
                                    binding.clTemplate.etAnswer2.text.toString(),
                                    binding.clTemplate.etAnswer3.text.toString(),
                                    binding.clTemplate.etAnswer4.text.toString(),
                                )
                        )

                        Timber.e("[ABC] 수정할 data 세팅 완료: $editWorryReqDTO")
                        viewModel.editWorry(editWorryReqDTO)
                    }
                }
            }
            layoutNetworkError.btnNetworkError.onSingleClick {
                viewModel.getTemplateDetailData()
            }
            layoutInternalError.btnInternalError.onSingleClick {
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
                        if (it is UiState.Success) { // TODO: 이렇게 하는 게.. 맞나
                            goToDetailBeforeActivity()
                            finish()
                        }
                    }
                }
                launch {
                    viewModel.editWorryFlow.collect {
                        Timber.e("[ABC] 글수정 $it")
                        if (it is UiState.Success) { // TODO: 이렇게 하는 게.. 맞나
                            startActivity(
                                Intent(applicationContext, DetailBeforeActivity::class.java).apply {
                                    putExtra("worryId", detailToEditData!!.worryId)
                                    putExtra("from", "edit")
                                }
                            )
                            finish()
                        }
                    }
                }
            }
        }
        // 마찬가지로 코루틴 열고 수집을 하는데 생명주기에 맞춰서 flow가 자동으로
        // 꺼지고 수집하고 될수 있도록 repeatOnLifeCycle이란걸 사용! 그리고
        // 뷰모델에 있는 StateFlow에서 뷰모델에서 해줬던것처럼 collect 해준다/
        // 여기 내부 값은 UiState가 들어오게 된다!
    }

    private fun render(uiState: UiState<TemplateDetailEntity>) {
        // 실제로 뷰에서 대응하는 함수 프로그래스바 visibility 조절, error msg 출력 등을 하면 된다!
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit // TODO: 추가해주세요 (written by. 수현)
            is UiState.Loading -> {
                binding.loadingBar.visible(true)
            }

            is UiState.Success -> {
                binding.loadingBar.visible(false)
                binding.clTitle.visible(true)
                binding.scrollView.visible(true)
                if (viewModel.templateIdFlow.value == Constant.freeNoteId) { // free flow
                    binding.templatedata = uiState.data
                    binding.clEmpty.root.visible(false)
                    binding.clTemplate.root.visible(false)
                    binding.clFreenote.root.visible(true)
                } else if (viewModel.templateIdFlow.value in 2..6) { // free flow 제외 나머지
                    binding.templatedata = uiState.data
                    binding.clEmpty.root.visible(false)
                    binding.clFreenote.root.visible(false)
                    binding.clTemplate.root.visible(true)
                    if (viewModel.templateIdFlow.value == Constant.thanksToId) binding.clTemplate.tvThanks.visible(
                        true
                    ) //thanksTo 템플릿
                    else binding.clTemplate.tvThanks.visible(false)
                }

                if (detailToEditData == null) clearEditText()
                else {
                    binding.etTitle.setText(detailToEditData!!.title)
                    if (templateId == Constant.freeNoteId) { // free flow
                        binding.clFreenote.etFreenote.setText(detailToEditData!!.answers.get(0))
                    } else {
                        binding.clTemplate.etAnswer1.setText(detailToEditData!!.answers.get(0))
                        binding.clTemplate.etAnswer2.setText(detailToEditData!!.answers.get(1))
                        binding.clTemplate.etAnswer3.setText(detailToEditData!!.answers.get(2))
                        binding.clTemplate.etAnswer4.setText(detailToEditData!!.answers.get(3))
                    }
                }
            }

            is UiState.Error -> {
                binding.clEmpty.root.visible(false)
                binding.loadingBar.visible(false)
                binding.scrollView.visible(false)
                binding.clTitle.visible(true)
                when (uiState.error) {
                    Constant.networkError -> {
                        binding.layoutNetworkError.root.visible(true)
                    }

                    Constant.internalError -> {
                        binding.layoutInternalError.root.visible(true)
                    }
                }
                binding.root.makeToast(uiState.error)
            }
        }
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

    private fun goToDetailBeforeActivity() {
        val worryDetailEntity = setWorryDetailEntity()
        // Timber.e("[ABC] $worryDetailEntity 이거 쓴 거당 이제 고민상세로")
        val json = Json.encodeToString(worryDetailEntity) // convert the object to a JSON string

        var bundle = Bundle()
        bundle.putString("worryDetailEntity", json)
        val intent = Intent(this@WriteActivity, DetailBeforeActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun setWorryDetailEntity(): WorryDetailEntity {
        val days = getDeadline(writeWorryReqDTO.deadline)
        val templateId = writeWorryReqDTO.templateId
        val subtitleList: List<String>
        val answerList: List<String>

        // 2) answers, subtitles 세팅
        if (templateId == Constant.freeNoteId) { // free flow
            subtitleList = listOf(binding.clFreenote.templatedata!!.questions[0])
            answerList = listOf(binding.clFreenote.etFreenote.text.toString())
        } else { // 일반 template
            subtitleList = binding.clTemplate.templatedata!!.questions
            answerList = listOf(
                binding.clTemplate.etAnswer1.text.toString(),
                binding.clTemplate.etAnswer2.text.toString(),
                binding.clTemplate.etAnswer3.text.toString(),
                binding.clTemplate.etAnswer4.text.toString(),
            )
        }

        return WorryDetailEntity(
            templateId = templateId,
            d_day = writeWorryReqDTO.deadline * -1,
            deadline = days[1],
            finalAnswer = null,
            period = "${days[0]}~${days[1]}",
            review = null,
            title = writeWorryReqDTO.title,
            updatedAt = days[0],
            answers = answerList,
            subtitles = subtitleList
        )
    }

    private fun getDetailToEditData() {
        val bundle = intent.extras
        if (bundle != null) { // DetailBeforeActivity -> WriteActivity 넘어온 것
            val json =
                bundle.getString("detailToEditData") // Retrieve the JSON string from the Bundle
            if (json != null) {
                detailToEditData = Json.decodeFromString<EditWorryReqDTO>(json)
                templateId = bundle.getInt("templateId")
                Timber.e("[ABC] 받은 data: $detailToEditData")
                viewModel.setTemplateId(templateId)
            }
        }
    }

    private fun showTemplateChoiceBottomSheet() {
        TemplateChoiceBottomSheet({
            viewModel.setTemplateId(it)
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