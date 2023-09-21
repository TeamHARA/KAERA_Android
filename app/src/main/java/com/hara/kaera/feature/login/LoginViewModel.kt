package com.hara.kaera.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.application.Constant.EMPTY_TOKEN
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.login.JWTRefreshReqDTO
import com.hara.kaera.data.dto.login.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
import com.hara.kaera.feature.util.tokenErrorHandle
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _kakaoRemoteJWT = MutableStateFlow<UiState<KakaoLoginJWTEntity>>(UiState.Init)
    val kakaoRemoteJWT = _kakaoRemoteJWT.asStateFlow()

    private val _tokenState = MutableStateFlow<TokenState<Nothing>>(TokenState.Init)
    val tokenState = _tokenState.asStateFlow()

    private val _localRefreshToken = MutableStateFlow("")
    val localRefreshToken = _localRefreshToken.asStateFlow()

    private val _localAccessToken = MutableStateFlow("")
    val localAccessToken = _localAccessToken.asStateFlow()

    init {
        getSavedRefreshToken()
    }

    /*
    데이터스토어에 저장되어있는 리프레시 토큰을 가져오는 함수
     */
    private fun getSavedRefreshToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getSavedRefreshToken()
            }.onSuccess {
                it.collect { token ->
                    Timber.e(token)
                    when (token) {
                        EMPTY_TOKEN -> {
                            _tokenState.value = TokenState.Empty
                        }

                        else -> {
                            _tokenState.value = TokenState.Exist
                            _localRefreshToken.value = token
                        }
                    }
                }
            }.onFailure {
                _tokenState.value = TokenState.Empty
                throw it
            }
        }
    }

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
                    Timber.e(apiresult.toString())

                    when (apiresult) {
                        is ApiResult.Success -> {
                            _kakaoRemoteJWT.value = UiState.Success(apiresult.data)
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
    액세스토큰 재발급 요청하는 함수
     */
    fun callUpdatedAccessToken() {
        viewModelScope.launch {
            runBlocking {
                loginRepository.getSavedAccessToken().collect {
                    _localAccessToken.value = it
                }
            }

            kotlin.runCatching {
                loginRepository.getUpdatedAccessToken(
                    JWTRefreshReqDTO(
                        accessToken = _localAccessToken.value,
                        refreshToekn = _localRefreshToken.value,
                    )
                )
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e(apiresult.toString())
                    when (apiresult) {
                        is ApiResult.Success -> {
                            kotlin.runCatching {
                                loginRepository.updateAccessToken(accessToken = apiresult.data)
                            }.onSuccess {
                                _tokenState.value = TokenState.Valid
                            }.onFailure {
                                throw it
                            }
                        }

                        is ApiResult.Error -> {
                            // 토큰이 유효하지 않거나 만료된 경우
                            tokenErrorHandle(apiresult.error)
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
    fun saveKakaoJWT(jwt: KakaoLoginJWTEntity) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.saveKaeraJWT(
                    accessToken = jwt.accessToken,
                    refreshToken = jwt.refreshToken
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

    sealed class TokenState<out T> {
        object Init : TokenState<Nothing>()
        object Empty : TokenState<Nothing>()
        object Valid : TokenState<Nothing>()
        object Exist : TokenState<Nothing>()
    }

}