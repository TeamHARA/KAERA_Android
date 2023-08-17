package com.hara.kaera.presentation.storage.worrytemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryTemplateViewModel @Inject constructor(
    private val usecase: GetTemplateTypeUseCase,
) : ViewModel() {
    private val _worryTemplateStateFLow =
        MutableStateFlow<UiState<TemplateTypesEntity>>(UiState.Init)
    val worryTemplateStateFlow = _worryTemplateStateFLow.asStateFlow()

    init {
        viewModelScope.launch {
            _worryTemplateStateFLow.value = UiState.Loading
            kotlin.runCatching {
                usecase.getTemplateFlow()
            }.onSuccess {
                it.collect { collect ->
                    if (collect.templateTypeList == null) {
                        _worryTemplateStateFLow.value = UiState.Error(collect.errorMessage!!)
                    } else {
                        _worryTemplateStateFLow.value = UiState.Success(collect)
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }
}
