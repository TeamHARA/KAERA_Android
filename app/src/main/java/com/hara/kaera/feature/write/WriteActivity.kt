package com.hara.kaera.feature.write

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.snackbar.KaeraSnackBar
import com.hara.kaera.feature.detail.DetailBeforeActivity
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.controlErrorLayout
import com.hara.kaera.feature.util.increaseTouchSize
import com.hara.kaera.feature.util.makeToast
import com.hara.kaera.feature.util.onSingleClick
import com.hara.kaera.feature.util.scrollableInScrollView
import com.hara.kaera.feature.util.stringOf
import com.hara.kaera.feature.util.visible
import com.hara.kaera.feature.write.custom.DialogBackpressWarning
import com.hara.kaera.feature.write.custom.DialogSaveWarning
import com.hara.kaera.feature.write.custom.DialogWriteComplete
import com.hara.kaera.feature.write.custom.TemplateChoiceBottomSheet
import com.hara.kaera.feature.write.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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

        mappingEditText()
        getDetailToEditData() // DetailBeforeActivity -> WriteActivity 넘어온 것인지 확인
        setTextWatcher()
        setClickListeners()
        collectFlows()
        setTemplate()
    }

    private fun mappingEditText() {

        editTextList = listOf(
            binding.clTemplate.etAnswer1,
            binding.clTemplate.etAnswer2,
            binding.clTemplate.etAnswer3,
            binding.clTemplate.etAnswer4
        )
        editTextFreeNote = binding.clFreenote.etFreenote
        edittextTitle = binding.etTitle
        editTextList.forEach {
            it.scrollableInScrollView()
        }
        editTextFreeNote.scrollableInScrollView()
    }

    private fun getDetailToEditData() {
        viewModel.setAction(intent.getStringExtra(ACTION_ID) ?: ACTION_WRITE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("worryDetail", WorryDetailEntity::class.java)
        } else {
            intent.getParcelableExtra("worryDetail") as WorryDetailEntity?
        }?.let { intentWorryDetail ->
            binding.userInput = intentWorryDetail
            viewModel.setTemplateId(intentWorryDetail.templateId)
        } ?: kotlin.run {
            showTemplateChoiceBottomSheet() // write aciton으로 activity 진입하면 bottom sheet 등장
        }
    }

    private fun setTemplate() {
        if (intent.hasExtra("templateId")) {
            viewModel.setTemplateId(intent.getIntExtra("templateId", -1))
        }
    }

    private fun setClickListeners() {
        binding.apply {
            with(btnClose) {
                increaseTouchSize(baseContext)
                setOnClickListener {
                    checkBackPressWarning()
                }
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

                when (viewModel.activityAction.value) {
                    ACTION_WRITE -> { // [글 작성]
                        DialogWriteComplete(
                            fun(day: Int) {
                                viewModel.writeWorry(
                                    title = binding.etTitle.text.toString(),
                                    answers = getAnswers(),
                                    dDay = day
                                )
                            }, ACTION_WRITE, -1
                        ).show(supportFragmentManager, "complete")
                    }

                    ACTION_EDIT -> { // [글 수정]
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
                                Intent(baseContext, DetailBeforeActivity::class.java).apply {
                                    putExtra("action", "write")
                                    it.data.subtitles =
                                        binding.templatedata!!.questions // [고민작성 API - response]에 subtitles는 안 넘어와서..
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
                                Intent(baseContext, DetailBeforeActivity::class.java).apply {
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
                launch {
                    viewModel.activityAction.collect {
                        when (it) {
                            ACTION_WRITE -> binding.btnComplete.text = "완료"
                            ACTION_EDIT -> binding.btnComplete.text = "수정"
                        }
                    }
                }

            }
        }
    }

    // [글 수정] EditText에 글 detail 내용 value를 넣는다
    private fun renderForEdit(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit
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

    private fun render(uiState: UiState<TemplateDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Empty -> Unit
            is UiState.Loading -> binding.layoutLoading.root.visible(true)

            is UiState.Success -> {
                controlLayout(true)
                binding.templatedata = uiState.data
                if (viewModel.templateIdFlow.value == Constant.freeNoteId) { // free flow
                    binding.clTemplate.root.visible(false)
                    binding.clFreenote.root.visible(true)
                } else if (viewModel.templateIdFlow.value in 2..6) { // 일반 template
                    binding.clFreenote.root.visible(false)
                    binding.clTemplate.root.visible(true)
                    when (viewModel.templateIdFlow.value) {
                        Constant.thanksToId -> binding.clTemplate.tvThanks.visible(true)
                        // thanksTo 템플릿
                        else -> binding.clTemplate.tvThanks.visible(false)
                    }
                }
            }

            is UiState.Error -> {
                controlLayout(false)
                controlErrorLayout(
                    error = uiState.error,
                    networkBinding = binding.layoutError.layoutNetworkError.root,
                    internalBinding = binding.layoutError.layoutInternalError.root,
                    root = binding.root
                )
                binding.root.makeToast(uiState.error)
            }
        }
    }

    private fun controlLayout(success: Boolean) {
        binding.layoutLoading.root.visible(false)
        binding.clEmpty.root.visible(false)
        binding.scrollView.visible(success)
        binding.layoutError.root.visible(!success)
    }


    private fun clearEditText() {
        // 제목 제외 초기화
        editTextList.forEach {
            it.text.clear()
        }
        editTextFreeNote.text.clear()
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
        TemplateChoiceBottomSheet({ id ->
            viewModel.setTemplateId(id)
            viewModel.setAction(ACTION_WRITE)
            clearEditText()
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