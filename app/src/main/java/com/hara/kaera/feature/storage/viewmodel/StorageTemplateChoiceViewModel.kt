package com.hara.kaera.feature.storage.viewmodel

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
class StorageTemplateChoiceViewModel @Inject constructor(
    private val useCase: GetTemplateTypeUseCase,
) : ViewModel() {

    private val _templateStateFlow =
        MutableStateFlow<UiState<TemplateTypesEntity>>(UiState.Init) // 초기상태는 UiState.Loading상태

    // 값이 주기적으로 바뀔수 있으므로 MuatbleStateFLow로
    val templateStateFlow = _templateStateFlow.asStateFlow()
    // View에서 값을 읽어야 하므로 변경불가능 타입인 StateFlow로 준다.

    init {
        getTemplateList()
    }

    fun getTemplateList(){
        viewModelScope.launch {
            _templateStateFlow.value = UiState.Loading
            kotlin.runCatching {
                useCase()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _templateStateFlow.value = UiState.Success(getAllTemplate(collect.data))
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

    private fun getAllTemplate(templateEntity: TemplateTypesEntity): TemplateTypesEntity {
        val templateList = mutableListOf(
            TemplateTypesEntity.Template(
                templateId = 0,
                title = "모든 보석 보기",
                info = "그동안 캐낸 모든 보석을 볼래요!",
                shortInfo = "그동안 캐낸 모든 보석을 볼래요!",
                hasUsed = false,
            ),
        )
        templateList.addAll(templateEntity.templateTypeList!!)
        return TemplateTypesEntity(errorMessage = null, templateTypeList = templateList)
    }
}
