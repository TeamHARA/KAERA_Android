package com.hara.kaera.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.application.Constant.EMPTY_TOKEN
import com.hara.kaera.core.ApiResult
import com.hara.kaera.core.ErrorType
import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.feature.util.TokenState
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _tokenState = MutableStateFlow<TokenState<Nothing>>(TokenState.Init)
    val tokenState = _tokenState.asStateFlow()

    private val _localRefreshToken = MutableStateFlow("")

    private val _localAccessToken = MutableStateFlow("")

    private var deviceToken = ""


    /*
    데이터스토어에 저장되어있는 리프레시 토큰을 가져오는 함수
     */
    fun getSavedRefreshToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getSavedRefreshToken().first() // 함수 호출당 딱 한번만 수집
            }.onSuccess { token ->
                when (token) {
                    EMPTY_TOKEN -> {
                        _tokenState.value = TokenState.Empty
                    }

                    else -> {
                        _tokenState.value = TokenState.Exist
                        _localRefreshToken.value = token
                        callUpdatedAccessToken()
                    }
                }
            }.onFailure {
                _tokenState.value = TokenState.Empty
                throw it
            }
        }
    }

    /*
    리프레시토큰이 만료되어서 다시 카카오로그인 이후 OAuth토큰을 기반으로 JWT를 요청하는 함수
     */
    fun callKakaoLoginJWT(oAuthToken: OAuthToken) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getKakaoLoginJTW(
                    accessToken = KaKaoLoginReqDTO(
                        accessToken = oAuthToken.accessToken,
                        deviceToken = deviceToken
                    )
                )
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e("callKakaoLoginJWT : $apiresult")

                    when (apiresult) {
                        is ApiResult.Success -> {
                            saveKakaoJWT(apiresult.data)
                        }

                        is ApiResult.Error -> {
                            _tokenState.value = TokenState.Error
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
    private fun callUpdatedAccessToken() {
        viewModelScope.launch {
            _localAccessToken.value = runBlocking {
                loginRepository.getSavedAccessToken().first()
            }

            kotlin.runCatching {
                Timber.e("refresh: ${_localRefreshToken.value}")
                Timber.e("access: ${_localAccessToken.value}")
                loginRepository.getUpdatedAccessToken(
                    JWTRefreshReqDTO(
                        accessToken = _localAccessToken.value,
                        refreshToken = _localRefreshToken.value,
                    )
                )
            }.onSuccess {
                it.collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Success -> {
                            kotlin.runCatching {
                                loginRepository.updateAccessToken(accessToken = apiResult.data)
                            }.onSuccess {
                                _tokenState.value = TokenState.Valid
                            }.onFailure { t ->
                                throw t
                            }
                        }

                        is ApiResult.Error -> {
                            // 토큰이 유효하지 않거나 만료된 경우
                            // 스플래시일 경우 로그인 액티비티를 이동하지 않으면서
                            // kakaoclient를 이용하여 로그인 후 OAuth토큰을 기반으로 JWT를 새로 받아와야함
                            when (apiResult.error) {
                                is ErrorType.Api.BadRequest -> {
                                    // 유효하지 않은 토큰
                                    Timber.e("token invalid")
                                    _tokenState.value = TokenState.Expired
                                }

                                is ErrorType.Api.UnAuthorized -> {
                                    // 모든 토큰 만료
                                    Timber.e("token expired")
                                    _tokenState.value = TokenState.Expired
                                }

                                else -> {
                                    // 토큰관련 아님
                                    Timber.e("not token error")
                                    _tokenState.value = TokenState.Error
                                    //errorToMessage(apiResult.error)
                                }
                            }
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
                    userId = jwt.userId,
                )
            }.onSuccess {
                Timber.e("datastore update success!!")
                _tokenState.value = TokenState.Valid
            }.onFailure {
                throw it
            }
        }
    }

    fun setDeviceToken(fcmToken: String) {
        deviceToken = fcmToken
    }

}