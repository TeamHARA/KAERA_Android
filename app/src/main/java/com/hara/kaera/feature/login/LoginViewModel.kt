package com.hara.kaera.feature.login

import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor() : ViewModel() {

    //private val _oAuthTokenFlow  = MutableStateFlow<UiSatae<서버리퀘스트바디>>(UiState.init)
    // val oAuthTokenFlow = _oAuthTokenFlow.asSatateFlow()

}