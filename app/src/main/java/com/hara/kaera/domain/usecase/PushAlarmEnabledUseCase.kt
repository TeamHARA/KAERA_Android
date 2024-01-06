package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.PushAlarmReqDTO
import kotlinx.coroutines.flow.Flow

interface PushAlarmEnabledUseCase {
    operator fun invoke(isTrued: Int, pushAlarmReqDTO: PushAlarmReqDTO): Flow<ApiResult<String>>
}