package com.hara.kaera.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.feature.util.UiState
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

    private val stoneList = MutableList(12) { HomeWorryListEntity.HomeWorry(worryId = -1, templateId = -1, title = "") }
    private var stoneListSize = 0
    private val jewelList = MutableList(12) { HomeWorryListEntity.HomeWorry(worryId = -1, templateId = -1, title = "") }
    private var jewelListSize = 0

    init {
        _homeWorryListStoneFlow.value = UiState.Loading

        // 서버 통신 1) 원석 (해결 전 고민들)
        viewModelScope.launch {
            _homeWorryListStoneFlow.value = UiState.Loading
            kotlin.runCatching {
                homeUseCase(0)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            // TODO: 아무 데이터가 없으면 null이 오는지, list가 empty로 오는지?
                            if (collect.data.homeWorryList.isEmpty()) {
                                _homeWorryListStoneFlow.value = UiState.Empty
                            }
                            else {
                                collect.data.homeWorryList.forEach { stone ->
                                    stoneList[Constant.homeGemsSequence[stoneListSize++]] = stone
                                }
                                _homeWorryListStoneFlow.value = UiState.Success(HomeWorryListEntity(stoneList))
                            }
                        }
                        is ApiResult.Error -> {
                            _homeWorryListStoneFlow.value = UiState.Error(collect.error.toString())
                        }
                    }
                }
            }.onFailure {
                throw it
                UiState.Error("[홈 화면/원석 display] 서버가 불안정합니다.")
            }
        }

        // 서버 통신 2) 보석 (해결 완 고민들)
        viewModelScope.launch {
            _homeWorryListJewelFlow.value = UiState.Loading
            kotlin.runCatching {
                homeUseCase(1)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            // TODO: 아무 데이터가 없으면 null이 오는지, list가 empty로 오는지?
                            if (collect.data.homeWorryList.isEmpty()) {
                                _homeWorryListJewelFlow.value = UiState.Empty
                            } else {
                                collect.data.homeWorryList.forEach { jewel ->
                                    jewelList[Constant.homeGemsSequence[jewelListSize++]] = jewel
                                }
                                _homeWorryListJewelFlow.value =
                                    UiState.Success(HomeWorryListEntity(jewelList))
                            }
                        }
                        is ApiResult.Error -> {
                            _homeWorryListJewelFlow.value = UiState.Error(collect.error.toString())
                        }
                    }
                }
            }.onFailure {
                throw it
                UiState.Error("[홈 화면/보석 display] 서버가 불안정합니다.")
            }
        }
    }
}