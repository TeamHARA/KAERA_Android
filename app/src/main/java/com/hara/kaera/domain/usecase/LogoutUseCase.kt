package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import kotlinx.coroutines.flow.Flow

interface LogoutUseCase {
    operator fun invoke(): Flow<ApiResult<Boolean>>
}