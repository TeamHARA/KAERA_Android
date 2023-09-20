package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>>

    suspend fun clearDataStore()

    fun getSavedAccessToken(): Flow<String>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveKaeraJWT(accessToken: String, refreshToken: String)
}