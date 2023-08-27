package com.hara.kaera.presentation.storage.worrytemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.presentation.util.errorToMessage
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
                usecase()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _worryTemplateStateFLow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _worryTemplateStateFLow.value =
                                UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("알 수 없는 오류 입니다.")
            }
        }
    }
}
