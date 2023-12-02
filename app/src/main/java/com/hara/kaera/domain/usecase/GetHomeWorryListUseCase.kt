package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.HomeWorryListEntity
import kotlinx.coroutines.flow.Flow

interface GetHomeWorryListUseCase {
    operator fun invoke(isSolved: Int, page: Int, limit: Int): Flow<ApiResult<HomeWorryListEntity>>
}
