package com.hara.kaera.data.datasource.remote

import com.hara.kaera.data.dto.login.JWTRefreshReqDTO
import com.hara.kaera.data.dto.login.JWTRefreshResDTO
import com.hara.kaera.data.dto.login.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.login.KaKaoLoginResDTO
import com.hara.kaera.data.util.safeCallApi
import com.hara.kaera.di.ServiceModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    @ServiceModule.LoginRetrofit private val loginApi: LoginApi
) : LoginDataSource {
    override fun getKakaoLoginJWT(accessToken: KaKaoLoginReqDTO): Flow<KaKaoLoginResDTO> {
        return flow {
            emit(loginApi.getKakaoLoginJWT(accessToken))
        }.safeCallApi()
    }

    override fun getAccessToken(tokens: JWTRefreshReqDTO): Flow<JWTRefreshResDTO> {
        return flow {
            emit(loginApi.getAccessToken(tokens))
        }.safeCallApi()
    }
}