package com.hara.kaera.data.datasource.remote

import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.data.dto.login.JWTRefreshResDTO
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.login.KaKaoLoginResDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/kakao/login")
    suspend fun getKakaoLoginJWT(
        @Body kaKaoLoginReqDTO: KaKaoLoginReqDTO
    ): KaKaoLoginResDTO

    @POST("auth/token/refresh")
    suspend fun getAccessToken(
        @Body jwtRefreshReqDTO: JWTRefreshReqDTO
    ):JWTRefreshResDTO

}