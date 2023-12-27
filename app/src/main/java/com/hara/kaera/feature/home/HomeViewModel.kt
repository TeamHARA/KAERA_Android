package com.hara.kaera.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: GetHomeWorryListUseCase
) : ViewModel() {

    init {
//        Timber.e("[ABC] HomeViewModel created!")
    }

    // 홈화면 원석 목록
    private val _homeWorryListStoneFlow = MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListStoneFlow = _homeWorryListStoneFlow.asStateFlow()

    // 홈화면 보석 목록
    private val _homeWorryListJewelFlow = MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListJewelFlow = _homeWorryListJewelFlow.asStateFlow()

    private val stoneList = MutableList(12) { HomeWorryListEntity.HomeWorry(worryId = -1, templateId = -1, title = "") }
    private var stoneListSize = 0
    private val jewelList = MutableList(12) { HomeWorryListEntity.HomeWorry(worryId = -1, templateId = -1, title = "") }
    private var jewelListSize = 0

    fun getHomeWorryList(isSolved: Boolean) {
        val flow = if (!isSolved) _homeWorryListStoneFlow else _homeWorryListJewelFlow
        val result = if (!isSolved) stoneList else jewelList
        var resultSize = if (!isSolved) stoneListSize else jewelListSize

        flow.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                homeUseCase(if (!isSolved) 0 else 1, firstPage, itemLimit)
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {

                        is ApiResult.Success -> {
                            // TODO: 아무 데이터가 없으면 null이 오는지, list가 empty로 오는지?
                            if (collect.data.homeWorryList.isEmpty()) {
                                flow.value = UiState.Empty
                            }
                            else {
                                collect.data.homeWorryList.forEachIndexed { index, gem ->
                                    // TODO: 서버에 원석이 12개 넘게 있을 때
                                    if(isSolved){ // 해결된 고민(보석함)의 경우 순서대로 배치되도록 수정
                                        if (resultSize < 12) result[index] = gem
                                    }else{
                                        if (resultSize < 12) result[Constant.homeGemsSequence[resultSize++]] = gem
                                    }
                                    
                                }
                                flow.value = UiState.Success(HomeWorryListEntity(result))
                            }
                        }
                        is ApiResult.Error -> {
                            flow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                throw it
                UiState.Error("[홈 화면/원석 display] 서버가 불안정합니다.")
            }
        }
    }

    companion object {
        const val firstPage = 1
        const val itemLimit = 12
    }
}