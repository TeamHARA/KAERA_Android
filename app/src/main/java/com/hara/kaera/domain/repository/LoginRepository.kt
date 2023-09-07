package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>>
}