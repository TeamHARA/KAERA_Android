package com.hara.kaera.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.core.ErrorType
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.EditDeadlineEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.DecideFinalUseCase
import com.hara.kaera.domain.usecase.DeleteWorryUseCase
import com.hara.kaera.domain.usecase.EditDeadlineUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
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

    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    private val _editDeadlineStateFlow = MutableStateFlow<UiState<EditDeadlineEntity>>(UiState.Init)
    val editDeadlineStateFlow = _editDeadlineStateFlow.asStateFlow()

    private val _deleteWorryFlow = MutableStateFlow<UiState<DeleteWorryEntity>>(UiState.Init)
    val deleteWorryFlow = _deleteWorryFlow.asStateFlow()

    private val _decideFinalFlow = MutableStateFlow<UiState<String>>(UiState.Init)
    val decideFinalFlow = _decideFinalFlow.asStateFlow()

    fun getWorryDetail(worryId: Int) {
        viewModelScope.launch {
            _detailStateFlow.value = UiState.Loading
            kotlin.runCatching {
                worryUseCase(worryId)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _detailStateFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            // fcm을 통해서 진입하는 경우를 위한 예외처리
                            Timber.e(collect.error.toString())
                            if (collect.error == ErrorType.Api.BadRequest) {
                                _detailStateFlow.value = UiState.Error(Constant.notExistedId)
                            } else {
                                _detailStateFlow.value = UiState.Error(errorToLayout(collect.error))
                            }
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw (it)
            }
        }
    }

    fun editDeadline(worryId: Int, editDayCount: Int) {
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
                            _editDeadlineStateFlow.value = UiState.Init
                        }

                        is ApiResult.Error -> {
                            _editDeadlineStateFlow.value =
                                UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw (it)
            }
        }
    }

    fun deleteWorry(worryId: Int) {
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
                UiState.Error("잠시 후 다시 시도해주세요")
                throw (it)
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
                            _decideFinalFlow.value =
                                UiState.Error(errorToMessage(collect.error))
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