package com.hara.kaera.feature.write.viewmodel

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
class TemplateChoiceViewModel @Inject constructor(
    private val getTemplateTypeUseCase: GetTemplateTypeUseCase,
) : ViewModel() {

    private val _templateStateFlow =
        MutableStateFlow<UiState<TemplateTypesEntity>>(UiState.Init) // 초기상태는 UiState.Loading상태

    // 값이 주기적으로 바뀔수 있으므로 MuatbleStateFLow로
    val templateStateFlow = _templateStateFlow.asStateFlow()
    // View에서 값을 읽어야 하므로 변경불가능 타입인 StateFlow로 준다.

    init {
        getTemplateList()
    }

    fun getTemplateList() {
        viewModelScope.launch {
            _templateStateFlow.value = UiState.Loading
            kotlin.runCatching {
                getTemplateTypeUseCase()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _templateStateFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _templateStateFlow.value = UiState.Error(errorToLayout(collect.error))
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