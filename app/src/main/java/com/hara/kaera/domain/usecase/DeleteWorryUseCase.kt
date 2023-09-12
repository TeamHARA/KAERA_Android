package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.DeleteWorryEntity
import kotlinx.coroutines.flow.Flow

interface DeleteWorryUseCase {
    operator fun invoke(worryId: Int): Flow<ApiResult<DeleteWorryEntity>>
}
