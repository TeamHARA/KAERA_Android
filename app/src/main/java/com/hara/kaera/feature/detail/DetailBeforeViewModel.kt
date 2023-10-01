package com.hara.kaera.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.DeleteWorryUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.PutReviewUseCase
import com.hara.kaera.feature.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailBeforeViewModel @Inject constructor(
    private val worryUseCase: GetWorryDetailUseCase,
    private val deleteUseCase: DeleteWorryUseCase,
) : ViewModel() {
    private var worryId = -1

    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    fun getWorryDetail(worryId: Int) {
        this.worryId = worryId
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
                            _detailStateFlow.value = UiState.Error(collect.error.toString())
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