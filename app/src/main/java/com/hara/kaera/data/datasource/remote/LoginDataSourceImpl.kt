package com.hara.kaera.data.datasource.remote

import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.KaKaoLoginResDTO
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
            emit(loginApi.getKakaoLoginJTW(accessToken))
        }.safeCallApi()
    }
}