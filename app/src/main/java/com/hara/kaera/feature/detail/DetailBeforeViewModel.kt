package com.hara.kaera.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditDeadlineResDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.DecideFinalUseCase
import com.hara.kaera.domain.usecase.DeleteWorryUseCase
import com.hara.kaera.domain.usecase.EditDeadlineUseCase
import com.hara.kaera.domain.usecase.EditWorryUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.PutReviewUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailBeforeViewModel @Inject constructor(
    private val worryUseCase: GetWorryDetailUseCase,
    private val editDeadlineUseCase: EditDeadlineUseCase,
    private val deleteUseCase: DeleteWorryUseCase,
    private val decideFinalUseCase: DecideFinalUseCase
) : ViewModel() {
    private var worryId = -1
    var templateId = -1

    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    private val _editDeadlineStateFlow = MutableStateFlow<UiState<EditDeadlineResDTO>>(UiState.Init)
    val editDeadlineStateFlow = _editDeadlineStateFlow.asStateFlow()

    private val _deleteWorryFlow = MutableStateFlow<UiState<DeleteWorryEntity>>(UiState.Init)
    val deleteWorryFlow = _deleteWorryFlow.asStateFlow()

    private val _decideFinalFlow = MutableStateFlow<UiState<String>>(UiState.Init)
    val decideFinalFlow = _decideFinalFlow.asStateFlow()

    lateinit var detailToEditData: EditWorryReqDTO // edit 위해 WriteActivity로 넘길 data

    fun getWorryId(): Int {
        return worryId
    }

    fun setWorryId(worryId: Int) {
        this.worryId = worryId
    }

    fun getWorryDetail() {
        viewModelScope.launch {
            _detailStateFlow.value = UiState.Loading
            kotlin.runCatching {
                worryUseCase(worryId)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _detailStateFlow.value = UiState.Success(collect.data)
                            detailToEditData = EditWorryReqDTO(
                                worryId = worryId,
                                templateId = collect.data.templateId,
                                title = collect.data.title,
                                answers = collect.data.answers
                            )
                            templateId = collect.data.templateId
                        }

                        is ApiResult.Error -> {
                            _detailStateFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    fun editDeadline(editDayCount: Int) {
        Timber.e("[ABC] editDayCount가 바뀌었당 ${editDayCount}")
        val editDeadlineReqDTO = EditDeadlineReqDTO(
            worryId = worryId,
            dayCount = editDayCount
        )
        viewModelScope.launch {
            kotlin.runCatching {
                editDeadlineUseCase(editDeadlineReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    Timber.e("ABC : ${collect}")
                    when (collect) {
                        is ApiResult.Success -> {
                            _editDeadlineStateFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _editDeadlineStateFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    fun deleteWorry() {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteUseCase(worryId)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _deleteWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _deleteWorryFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    fun decideFinal(decideFinalReqDTO: DecideFinalReqDTO) {
        viewModelScope.launch {
            kotlin.runCatching {
                decideFinalUseCase(decideFinalReqDTO)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _decideFinalFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _decideFinalFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }
}