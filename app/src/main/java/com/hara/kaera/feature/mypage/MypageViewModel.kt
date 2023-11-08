package com.hara.kaera.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.application.Constant
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val serviceLogoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _savedName = MutableStateFlow(Constant.EMPTY_NAME)
    val savedName get() = _savedName.asStateFlow()


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

    fun clearDataStore() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.clearDataStore()
            }.onSuccess {
                Timber.e("clear")
            }.onFailure {
                throw it
            }
        }
    }

    fun serviceLogout() {
        viewModelScope.launch {
            kotlin.runCatching {
                serviceLogoutUseCase.invoke()
            }.onSuccess {
                it.collect {
                    Timber.e("logout : $it")
                }
                clearDataStore()
            }.onFailure {
                Timber.e(it)
                throw it
            }
        }
    }


}
