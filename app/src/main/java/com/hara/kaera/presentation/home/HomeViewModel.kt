package com.hara.kaera.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: GetHomeWorryListUseCase
) : ViewModel() {
    private val _homeWorryListStoneFlow = MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListStoneFlow = _homeWorryListStoneFlow.asStateFlow()

    private val _homeWorryListJewelFlow = MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListJewelFlow = _homeWorryListJewelFlow.asStateFlow()

    init {
        _homeWorryListStoneFlow.value = UiState.Loading
        // 서버 통신 1) 원석 (해결 전 고민들)
        viewModelScope.launch {
            kotlin.runCatching {
                homeUseCase.getHomeWorryListFlow(0)
            }.onSuccess {
                it.collect { collect ->
                    if (collect.data?.isEmpty() == true)
                        _homeWorryListStoneFlow.value = UiState.Error("에러")
                    else _homeWorryListStoneFlow.value = UiState.Success(collect)
                }
            }.onFailure {
                throw it
            }
        }
        // 서버 통신 2) 보석 (해결 완 고민들)
        viewModelScope.launch {
            kotlin.runCatching {
                homeUseCase.getHomeWorryListFlow(1)
            }.onSuccess {
                it.collect {
                    if (it.data?.isEmpty() == true)
                        _homeWorryListJewelFlow.value = UiState.Error("에러")
                    else _homeWorryListJewelFlow.value = UiState.Success(it)
                }
            }.onFailure {
                throw it
            }
        }
    }
}