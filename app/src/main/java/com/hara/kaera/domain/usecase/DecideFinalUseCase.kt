package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.DecideFinalReqDTO
import kotlinx.coroutines.flow.Flow

interface DecideFinalUseCase {
    operator fun invoke(decideFinalReqDTO: DecideFinalReqDTO): Flow<ApiResult<String>>
}