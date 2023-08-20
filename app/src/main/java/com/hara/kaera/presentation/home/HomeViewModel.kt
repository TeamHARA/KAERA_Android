package com.hara.kaera.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.presentation.util.Constant
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

    private val stoneList = arrayOfNulls<HomeWorryListEntity.HomeWorry?>(12).toMutableList()
    private var stoneListSize = 0
    private val jewelList = arrayOfNulls<HomeWorryListEntity.HomeWorry?>(12).toMutableList()
    private var jewelListSize = 0

    init {
        _homeWorryListStoneFlow.value = UiState.Loading

        // 서버 통신 1) 원석 (해결 전 고민들)
        viewModelScope.launch {
            kotlin.runCatching {
                homeUseCase.getHomeWorryListFlow(0)
            }.onSuccess {
                it.collect {
                    if (it.data?.isEmpty() == true)
                        _homeWorryListStoneFlow.value = UiState.Error("에러")
                    else {
                        it.data?.forEach {
                            stoneList[Constant.homeGemsSequence[stoneListSize++]] = it
                        }
                        _homeWorryListStoneFlow.value = UiState.Success(HomeWorryListEntity(stoneList))
                    }
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
                    else {
                        it.data?.forEach {
                            jewelList[Constant.homeGemsSequence[jewelListSize++]] = it
                        }
                        _homeWorryListJewelFlow.value = UiState.Success(HomeWorryListEntity(jewelList))
                    }
                }
            }.onFailure {
                throw it
            }
        }
    }
}