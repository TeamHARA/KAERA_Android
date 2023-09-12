package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryDetailEntity
import kotlinx.coroutines.flow.Flow

interface GetWorryDetailUseCase {
    operator fun invoke(worryId: Int): Flow<ApiResult<WorryDetailEntity>>
}
