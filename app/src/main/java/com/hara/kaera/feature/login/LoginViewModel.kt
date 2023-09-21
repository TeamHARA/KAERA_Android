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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _kakaoJWT = MutableStateFlow<UiState<KakaoLoginJWTEntity>>(UiState.Init)
    val kakaoJWT = _kakaoJWT.asStateFlow()

    /*
    나중에는 리프레시 토큰이 들어가서 accesstoken을 갱신하는 식으로 가야할듯
     */
    private val _localRefreshToken = MutableStateFlow<TokenState<String>>(TokenState.Init)
    val localRefreshToken = _localRefreshToken.asStateFlow()

    private val _localAccessToken = MutableStateFlow<String>("")
    val localAccessToken = _localAccessToken.asStateFlow()

    init {
        getSavedRefreshToken()
    }

    fun callKakaoLoginJWT(oAuthToken: OAuthToken) {
        _kakaoJWT.value = UiState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getKakaoLoginJTW(accessToken = KaKaoLoginReqDTO(accessToken = oAuthToken.accessToken))
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e(apiresult.toString())

                    when (apiresult) {
                        is ApiResult.Success -> {
                            _kakaoJWT.value = UiState.Success(apiresult.data)
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

    fun callUpdatedAccessToken(refreshToken: String) {
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
                        refreshToekn = refreshToken,
                    )
                )
            }.onSuccess {
                it.collect { apiresult ->
                    Timber.e(apiresult.toString())

                    when (apiresult) {
                        is ApiResult.Success -> {
                            loginRepository.updateAccessToken(apiresult.data)
                            _localRefreshToken.value = TokenState.Valid
                        }

                        is ApiResult.Error -> {
                            tokenErrorHandle(apiresult.error)
                        }

                    }
                }
            }.onFailure {
                throw it
            }
        }

    }

    fun saveKakaoJWT(jwt: KakaoLoginJWTEntity) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.saveKaeraJWT(
                    accessToken = jwt.accessToken,
                    refreshToken = jwt.refreshToken
                )
            }.onSuccess {
                Timber.e("datastore update success!!")
                _localRefreshToken.value = TokenState.Valid
            }.onFailure {
                throw it
            }
        }

    }

    fun saveAccessToken(accessToken: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.updateAccessToken(
                    accessToken = accessToken
                )
            }.onSuccess {
                Timber.e("datastore accessToeknSave success!!")
            }.onFailure {
                throw it
            }
        }
    }

    private fun getSavedRefreshToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                loginRepository.getSavedRefreshToken()
            }.onSuccess {
                it.collect { token ->
                    Timber.e(token)
                    when (token) {
                        EMPTY_TOKEN -> {
                            _localRefreshToken.value = TokenState.Empty
                        }

                        else -> {
                            _localRefreshToken.value = TokenState.Exist(token)
                        }
                    }
                }
            }.onFailure {
                _localRefreshToken.value = TokenState.Empty
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
        data class Exist<String>(val token: String) : TokenState<String>()
    }

}