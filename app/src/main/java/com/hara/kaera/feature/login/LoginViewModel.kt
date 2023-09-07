package com.hara.kaera.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.usecase.GetKakaoLoginJWTUseCase
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoUseCase: GetKakaoLoginJWTUseCase
) : ViewModel() {

    //    private val _oAuthTokenFlow  = MutableStateFlow<OAuthToken>(OAuthToken())
//    val oAuthTokenFlow = _oAuthTokenFlow.asStateFlow()
    private val _kakaoJWT = MutableStateFlow<UiState<String>>(UiState.Init)
    val kakaoJWT = _kakaoJWT.asStateFlow()

    fun getKakaoLoginJWT(oAuthToken: OAuthToken) {
        _kakaoJWT.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                kakaoUseCase(accessToken = KaKaoLoginReqDTO(accessToken = oAuthToken.accessToken))
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e(apiresult.toString())

                    when (apiresult) {
                        is ApiResult.Success -> {
                            _kakaoJWT.value = UiState.Success(apiresult.data.accessToken)
                        }

                        is ApiResult.Error -> {
                            _kakaoJWT.value = UiState.Error(errorToMessage(apiresult.error))
                        }
                    }
                }
            }.onFailure {
                throw it
            }
        }
    }
}