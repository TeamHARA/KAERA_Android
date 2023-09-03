package com.hara.kaera.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.usecase.DeleteWorryUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.PutReviewUseCase
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAfterViewModel @Inject constructor(
    private val useCase: GetWorryDetailUseCase,
    private val deleteUseCase: DeleteWorryUseCase,
    private val reviewUseCase: PutReviewUseCase,
) : ViewModel() {
    private var worryId = -1
    private val _detailStateFlow = MutableStateFlow<UiState<WorryDetailEntity>>(UiState.Init)
    val detailStateFlow = _detailStateFlow.asStateFlow()
    private val _deleteWorryFlow = MutableStateFlow<UiState<DeleteWorryEntity>>(UiState.Init)
    val deleteWorryFlow = _deleteWorryFlow.asStateFlow()
    private val _reviewWorryFlow = MutableStateFlow<UiState<ReviewResEntity>>(UiState.Init)
    val reviewWorryFlow = _reviewWorryFlow.asStateFlow()

    fun getWorryDetail(worryId: Int) {
        this.worryId = worryId
        viewModelScope.launch {
            _detailStateFlow.value = UiState.Loading
            kotlin.runCatching {
                useCase(worryId)
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

    fun deleteWorry() {
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
                            _deleteWorryFlow.value = UiState.Error(collect.error.toString())
                        }
                    }
                }
            }.onFailure {
                throw (it)
                UiState.Error("서버가 불안정합니다.")
            }
        }
    }

    fun updateReview(review: String) {
        viewModelScope.launch {
            _reviewWorryFlow.value = UiState.Loading
            kotlin.runCatching {
                reviewUseCase(ReviewReqDTO(worryId, review))
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _reviewWorryFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _reviewWorryFlow.value = UiState.Error(collect.error.toString())
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
