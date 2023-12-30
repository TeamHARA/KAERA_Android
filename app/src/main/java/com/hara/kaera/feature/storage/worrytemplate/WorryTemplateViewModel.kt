package com.hara.kaera.feature.storage.worrytemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryTemplateViewModel @Inject constructor(
    private val useCase: GetTemplateTypeUseCase,
) : ViewModel() {
    private val _worryTemplateStateFLow =
        MutableStateFlow<UiState<TemplateTypesEntity>>(UiState.Init)
    val worryTemplateStateFlow = _worryTemplateStateFLow.asStateFlow()

    init {
        getWorryTemplates()
    }

    fun getWorryTemplates() {
        viewModelScope.launch {
            _worryTemplateStateFLow.value = UiState.Loading
            kotlin.runCatching {
                useCase()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _worryTemplateStateFLow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _worryTemplateStateFLow.value =
                                UiState.Error(errorToLayout(collect.error))
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
