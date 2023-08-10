package com.hara.kaera.presentation.write.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateChoiceViewModel @Inject constructor(
    private val useCase: GetTemplateTypeUseCase,
) : ViewModel() {

    private val _templateStateFlow =
        MutableStateFlow<UiState>(UiState.Loading) // 초기상태는 UiState.Loading상태

    // 값이 주기적으로 바뀔수 있으므로 MuatbleStateFLow로
    val templateStateFlow = _templateStateFlow.asStateFlow()
    // View에서 값을 읽어야 하므로 변경불가능 타입인 StateFlow로 준다.

    init {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getTemplateFlow()
            }.onSuccess {
                it.collect { collect ->
                    if (collect.templateTypeList == null) _templateStateFlow.value =
                        UiState.Error(collect.errorMessage!!)
                    else _templateStateFlow.value = UiState.Success(collect.templateTypeList)
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

}