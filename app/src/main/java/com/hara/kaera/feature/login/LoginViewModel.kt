package com.hara.kaera.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.feature.util.TokenState
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _kakaoRemoteJWT = MutableStateFlow<UiState<Unit>>(UiState.Init)
    val kakaoRemoteJWT = _kakaoRemoteJWT.asStateFlow()

    private val _tokenState = MutableStateFlow<TokenState<Nothing>>(TokenState.Init)
    val tokenState = _tokenState.asStateFlow()

    /*
    카카오로그인 이후 OAuth토큰을 기반으로 JWT를 요청하는 함수
     */
    fun callKakaoLoginJWT(oAuthToken: OAuthToken) {
        _kakaoRemoteJWT.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getKakaoLoginJTW(accessToken = KaKaoLoginReqDTO(accessToken = oAuthToken.accessToken))
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e("callKakaoLoginJWT : $apiresult")

                    when (apiresult) {
                        is ApiResult.Success -> {
                            saveKakaoJWT(apiresult.data)
                            _kakaoRemoteJWT.value = UiState.Success(Unit)
                        }

                        is ApiResult.Error -> {
                            _kakaoRemoteJWT.value = UiState.Error(errorToMessage(apiresult.error))
                        }

                    }
                }
            }.onFailure {
                throw it
            }
        }
    }


    /*
    JWT를 데이터스토어에 저장하는 함수
     */
    private fun saveKakaoJWT(jwt: KakaoLoginJWTEntity) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.saveKaeraJWT(
                    accessToken = jwt.accessToken,
                    refreshToken = jwt.refreshToken,
                    name = jwt.name,
                )
            }.onSuccess {
                Timber.e("datastore update success!!")
                _tokenState.value = TokenState.Valid
            }.onFailure {
                throw it
            }
        }
    }

    fun kakaoLogOut() {
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


}