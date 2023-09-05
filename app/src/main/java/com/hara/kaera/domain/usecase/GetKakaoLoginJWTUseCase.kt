package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import kotlinx.coroutines.flow.Flow

interface GetKakaoLoginJWTUseCase {
    operator fun invoke(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>>
}