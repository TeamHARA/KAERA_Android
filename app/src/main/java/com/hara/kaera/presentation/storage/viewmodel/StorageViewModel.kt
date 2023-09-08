package com.hara.kaera.presentation.storage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.usecase.GetWorryByTemplateUseCase
import com.hara.kaera.presentation.util.UiState
import com.hara.kaera.presentation.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val useCase: GetWorryByTemplateUseCase,
) : ViewModel() {
    private val _worryStateFlow = MutableStateFlow<UiState<WorryByTemplateEntity>>(UiState.Init)
    val worryStateFlow = _worryStateFlow.asStateFlow()

    private val _templateId = MutableLiveData(0)
    private val _selectedId = MutableLiveData<Int>()

    val templateId get() = _templateId
    val selectedId get() = _selectedId

    fun setTemplateId() {
        _templateId.value = _selectedId.value
    }

    fun setSelectedId(choiceId: Int) {
        _selectedId.value = choiceId
    }

    fun getJewels() {
        viewModelScope.launch {
            _worryStateFlow.value = UiState.Loading
            kotlin.runCatching {
                useCase(_templateId.value?.toInt() ?: 0)
            }.onSuccess {
                it.collect { collect ->
                    when(collect) {
                        is ApiResult.Success -> {
                            _worryStateFlow.value = UiState.Success(collect.data)
                        }
                        is ApiResult.Error -> {
                            _worryStateFlow.value = UiState.Error(errorToMessage(collect.error))
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
