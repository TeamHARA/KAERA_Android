package com.hara.kaera.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hara.kaera.application.Constant.EMPTY_TOKEN
import com.hara.kaera.core.ApiResult
import com.hara.kaera.core.ErrorType
import com.hara.kaera.data.dto.login.JWTRefreshReqDTO
import com.hara.kaera.data.dto.login.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.feature.util.UiState
import com.hara.kaera.feature.util.errorToMessage
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
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _kakaoRemoteJWT = MutableStateFlow<UiState<Unit>>(UiState.Init)
    val kakaoRemoteJWT = _kakaoRemoteJWT.asStateFlow()

    private val _tokenState = MutableStateFlow<TokenState<Nothing>>(TokenState.Init)
    val tokenState = _tokenState.asStateFlow()

    private val _localRefreshToken = MutableStateFlow("")

    private val _localAccessToken = MutableStateFlow("")

    init {
        getSavedRefreshToken()
    }

    /*
    데이터스토어에 저장되어있는 리프레시 토큰을 가져오는 함수
     */
    private fun getSavedRefreshToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getSavedRefreshToken().first() // 함수 호출당 딱 한번만 수집 
                // 이유는 해당 함수는 토큰이 있냐 없냐 검사후 다시 수집하면안됨
                // 토큰이 없으면 TokenState.Empty로 간 후 최초 로그인 로직(카카오로그인 -> JWT발급 -> 저장 -> 홈화면이동)
                // 토큰이 있으면 TokenState.Exist(밑에 보면 알겠지만 미리 refreshToken은 저장) 이후 재발급로직(재발급 서버통신 -> accessToken 리스폰-> accessToken만 갱신 -> 홈화면 이동)
            }.onSuccess { token ->
                Timber.e("getSavedRefreshToken: $token")
                when (token) {
                    EMPTY_TOKEN -> {
                        _tokenState.value = TokenState.Empty
                    }

                    else -> {
                        _tokenState.value = TokenState.Exist
                        _localRefreshToken.value = token
                        callUpdatedAccessToken()
                        Timber.e(_localRefreshToken.value)
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
                it.collect { apiresult ->
                    when (apiresult) {
                        is ApiResult.Success -> {
                            Timber.e("callUpdatedAccessToken : ${apiresult.data}")
                            kotlin.runCatching {
                                loginRepository.updateAccessToken(accessToken = apiresult.data)
                            }.onSuccess {
                                _tokenState.value = TokenState.Valid
                            }.onFailure {
                                throw it
                            }
                        }

                        is ApiResult.Error -> {
                            Timber.e("callUpdatedAccessToken : ${apiresult.error}")
                            // 토큰이 유효하지 않거나 만료된 경우
                            // TODO 스플래시일 경우 로그인 액티비티를 이동하지 않으면서 
                            // kakaoclient를 이용하여 로그인 후 OAuth토큰을 기반으로 JWT를 새로 받아와야함
                            // kakaoclinet.lio
                            when (apiresult.error) {
                                is ErrorType.Api.BadRequest -> {
                                    //TODO 유효하지 않은 토큰
                                    Timber.e("token invalid")
                                    _tokenState.value = TokenState.Expired
                                }

                                is ErrorType.Api.UnAuthorized -> {
                                    //TODO 모든 토큰 만료
                                    Timber.e("token expired")
                                    _tokenState.value = TokenState.Expired
                                }

                                else -> {
                                    //TODO 토큰관련 아님
                                    Timber.e("not token error")
                                    errorToMessage(apiresult.error)
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
        object Exist : TokenState<Nothing>()
        object Valid : TokenState<Nothing>()
        object Expired : TokenState<Nothing>()

    }

}