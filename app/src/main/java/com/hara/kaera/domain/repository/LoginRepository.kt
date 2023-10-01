package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>>

    fun getUpdatedAccessToken(tokens: JWTRefreshReqDTO): Flow<ApiResult<String>>

    suspend fun clearDataStore()

    fun getSavedRefreshToken(): Flow<String>

    fun getSavedAccessToken(): Flow<String>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun saveKaeraJWT(accessToken: String, refreshToken: String, name: String)
}