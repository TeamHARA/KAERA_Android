package com.hara.kaera.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.PushAlarmReqDTO
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.domain.usecase.PushAlarmEnabledUseCase
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToLayout
import com.hara.kaera.feature.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: GetHomeWorryListUseCase,
    private val pushAlarmEnabledUseCase: PushAlarmEnabledUseCase
) : ViewModel() {

    init {
//        Timber.e("[ABC] HomeViewModel created!")
    }

    // 홈화면 원석 목록
    private val _homeWorryListStoneFlow =
        MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListStoneFlow = _homeWorryListStoneFlow.asStateFlow()

    // 홈화면 보석 목록
    private val _homeWorryListJewelFlow =
        MutableStateFlow<UiState<HomeWorryListEntity>>(UiState.Init)
    val homeWorryListJewelFlow = _homeWorryListJewelFlow.asStateFlow()

    private var fullStone = false

    private val _viewPagerPosition = MutableStateFlow(0)
    val viewPagerPosition = _viewPagerPosition.asStateFlow()

    fun setViewPagerPosition(position: Int) {
        _viewPagerPosition.value = position
    }

    fun getHomeWorryList(isSolved: Boolean) {
        val flow = if (!isSolved) _homeWorryListStoneFlow else _homeWorryListJewelFlow
        val result = MutableList(12) {
            HomeWorryListEntity.HomeWorry(
                worryId = -1,
                templateId = -1,
                title = ""
            )
        }

        viewModelScope.launch {
            flow.value = UiState.Loading
            kotlin.runCatching {
                homeUseCase(if (!isSolved) 0 else 1, firstPage, itemLimit)
            }.onSuccess { it ->
                it.collect { collect ->
                    when (collect) {

                        is ApiResult.Success -> {
                            // TODO: 아무 데이터가 없으면 null이 오는지, list가 empty로 오는지?
                            if (collect.data.homeWorryList.isEmpty()) {
                                flow.value = UiState.Empty
                            } else {
                                collect.data.homeWorryList.forEachIndexed { index, gem ->
                                    if (isSolved) { // 해결된 고민(보석함)의 경우 순서대로 배치되도록 수정
                                        result[index] = gem
                                    } else {
                                        result[Constant.homeGemsSequence[index]] = gem
                                        fullStone = !(result.any { it.worryId == -1 })
                                    }

                                }
                                flow.value = UiState.Success(HomeWorryListEntity(result))
                            }
                        }

                        is ApiResult.Error -> {
                            flow.value = UiState.Error(errorToLayout(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw it
            }
        }
    }

    fun isFullStone(): Boolean {
        return fullStone
    }

    companion object {
        const val firstPage = 1
        const val itemLimit = 12
    }

    /*
메인에서 권한 허용이후 해당 함수를 중복호출 하는 문제가 있어서 우선 보류
 */

    private val _pushAlarmActivatedFlow =
        MutableStateFlow<UiState<String>>(UiState.Init)
    val pushAlarmActivatedFlow = _pushAlarmActivatedFlow.asStateFlow()
    fun pushAlarmActivated(deviceToken: String) {
        if (deviceToken == "null") return
        viewModelScope.launch {
            kotlin.runCatching {
                pushAlarmEnabledUseCase(1, PushAlarmReqDTO(deviceToken))
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _pushAlarmActivatedFlow.value = UiState.Success(collect.data)
                        }

                        is ApiResult.Error -> {
                            _pushAlarmActivatedFlow.value =
                                UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("마이페이지에서 알림 허용해주세요")
                throw it
            }
        }
    }
}