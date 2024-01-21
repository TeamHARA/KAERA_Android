package com.hara.kaera.feature.storage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.usecase.GetWorryByTemplateUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
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

    private val _templateId = MutableStateFlow(0)
    val templateId get() = _templateId.asStateFlow()

    private val _selectedId = MutableStateFlow(0)
    val selectedId get() = _selectedId.asStateFlow()

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
                useCase(_templateId.value)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            if (collect.data.totalNum == 0) {
                                _worryStateFlow.value = UiState.Empty
                            } else {
                                _worryStateFlow.value = UiState.Success(collect.data)
                            }
                        }

                        is ApiResult.Error -> {
                            _worryStateFlow.value = UiState.Error(errorToLayout(collect.error))
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
