package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import kotlinx.coroutines.flow.Flow

interface PushAlarmEnabledUseCase {
    operator fun invoke(isTrued: Int, pushAlarmReqDTO: String): Flow<ApiResult<String>>
}