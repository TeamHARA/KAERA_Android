package com.hara.kaera.feature.write.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.EditWorryUseCase
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.WriteWorryUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
import com.hara.kaera.feature.util.errorToMessage
import com.hara.kaera.feature.write.WriteActivity.Companion.ACTION_WRITE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val detailUseCase: GetTemplateDetailUseCase,
    private val writeWorryUseCase: WriteWorryUseCase,
    private val editWorryUseCase: EditWorryUseCase,
    private val worryUseCase: GetWorryDetailUseCase,
) : ViewModel() {

    private val _templateDetailFlow = MutableStateFlow<UiState<TemplateDetailEntity>>(UiState.Init)
    val templateDetailFlow = _templateDetailFlow.asStateFlow()

    private val _templateIdFlow = MutableStateFlow(-1)
    val templateIdFlow = _templateIdFlow.asStateFlow()
    private val _curTemplateIdFlow = MutableStateFlow(-1)
    val curTemplateIdFlow = _curTemplateIdFlow.asStateFlow()

    // 액티비티의 액션을 나타내는 flow write | edit
    private val _activityAction = MutableStateFlow(ACTION_WRITE)
    val activityAction = _activityAction.asStateFlow()

    // 글 작성 결과
    private val _writeWorryFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val writeWorryFlow = _writeWorryFlow.asStateFlow()

    // 글 수정 결과
    private val _editWorryFlow = MutableStateFlow<UiState<String>>(UiState.Init)
    val editWorryFlow = _editWorryFlow.asStateFlow()

    /* 글 수정 시에만 사용 */
    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    //repeatOnLifeCycle에 따라서 백그라운드로 나갔다 다시 create되면
    //onStarted에서 tmplateIdFlow를 다시 collect함 그에 따라서
    // getTemplateDetailData함수를 재호출함
    // 그에따라 새로 render함수도 호출 되고 이전에 작성한 글이 render가 다시 되어서 작성했던 글들이 삭제되어 버림
    // 이를 방지하기 위해서 나갔다 들어와도 재호출되지 않도록 singleSelecteAdapter 처럼 이전 선택한
    // id를 저장해두었다가 조건검사에 써준다

    fun setTemplateId(choiceId: Int) {
        _templateIdFlow.value = choiceId
    }

    fun setCurTemplateId(choiceId: Int) {
        _curTemplateIdFlow.value = choiceId
    }

    fun setAction(action: String) {
        _activityAction.value = action
    }

    fun getTemplateDetailData() {
        viewModelScope.launch {
            _templateDetailFlow.value = UiState.Loading
            kotlin.runCatching {
                detailUseCase(templateIdFlow.value)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _templateDetailFlow.value = UiState.Success(collect.data)
                            _curTemplateIdFlow.value = -1
                        }

                        is ApiResult.Error -> {
                            _templateDetailFlow.value = UiState.Error(errorToLayout(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw it
            }
        }
    }

    fun writeWorry(title: String, answers: List<String>, dDay: Int) {

        val writeWorryReqDTO = WriteWorryReqDTO(
            templateId = _templateIdFlow.value,
            title = title,
            answers = answers,
            deadline = dDay
        )

        viewModelScope.launch {
            _writeWorryFlow.value = UiState.Loading
            kotlin.runCatching {
                writeWorryUseCase(writeWorryReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _writeWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _writeWorryFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw (it)
            }
        }
    }

    fun editWorry(worryId: Int, title: String, answers: List<String>) {

        val editWorryReqDTO = EditWorryReqDTO(
            worryId = worryId,
            templateId = _templateIdFlow.value,
            title = title,
            answers = answers
        )

        viewModelScope.launch {
            _editWorryFlow.value = UiState.Loading
            kotlin.runCatching {
                editWorryUseCase(editWorryReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _editWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _editWorryFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw (it)
            }
        }
    }

}