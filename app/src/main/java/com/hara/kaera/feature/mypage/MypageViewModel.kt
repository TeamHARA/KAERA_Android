package com.hara.kaera.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.application.Constant
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.domain.usecase.LogoutUseCase
import com.hara.kaera.domain.usecase.UnRegisterUseCase
import com.hara.kaera.feature.mypage.MypageActivity.SignOutType
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val serviceLogoutUseCase: LogoutUseCase,
    private val serviceUnRegisterUseCase: UnRegisterUseCase
) : ViewModel() {

    private val _savedName = MutableStateFlow(Constant.EMPTY_NAME)
    val savedName get() = _savedName.asStateFlow()

    private val _uiStateFlow =
        MutableStateFlow<UiState<SignOutType<Unit>>>(UiState.Init)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getSavedName().first()
            }.onSuccess {
                _savedName.value = it
            }.onFailure {
                throw it
            }
        }
    }

    private fun clearDataStore() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.clearDataStore()
            }.onSuccess {
                Timber.e("clear")
                _uiStateFlow.value = UiState.Success(SignOutType.CLEAR)
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw it
            }
        }
    }

    fun serviceLogout() {
        viewModelScope.launch {
            _uiStateFlow.value = UiState.Loading
            kotlin.runCatching {
                serviceLogoutUseCase.invoke()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _uiStateFlow.value = UiState.Success(SignOutType.LOGOUT)
                            clearDataStore()
                        }

                        is ApiResult.Error -> {
                            _uiStateFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw it
            }
        }
    }

    fun serviceUnRegister() {
        viewModelScope.launch {
            _uiStateFlow.value = UiState.Loading
            kotlin.runCatching {
                serviceUnRegisterUseCase.invoke()
            }.onSuccess {
                it.collect { collect ->
                    when (collect) {
                        is ApiResult.Success -> {
                            _uiStateFlow.value = UiState.Success(SignOutType.UNLINK)
                            clearDataStore()
                        }

                        is ApiResult.Error -> {
                            _uiStateFlow.value = UiState.Error(errorToMessage(collect.error))
                        }
                    }
                }
            }.onFailure {
                UiState.Error("잠시 후 다시 시도해주세요")
                throw it
            }
        }
    }


}
