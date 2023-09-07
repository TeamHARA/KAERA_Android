package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.KaKaoLoginResDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("user/kakao/login")
    suspend fun getKakaoLoginJTW(
        @Body kaKaoLoginReqDTO: KaKaoLoginReqDTO
    ): KaKaoLoginResDTO
}