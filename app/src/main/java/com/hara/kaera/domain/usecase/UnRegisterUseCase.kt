package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import kotlinx.coroutines.flow.Flow

interface UnRegisterUseCase {
    operator fun invoke(): Flow<ApiResult<Boolean>>
}