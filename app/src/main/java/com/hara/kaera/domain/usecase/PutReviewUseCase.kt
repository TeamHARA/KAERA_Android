package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.domain.entity.ReviewResEntity
import kotlinx.coroutines.flow.Flow

interface PutReviewUseCase {
    operator fun invoke(reviewReqDTO: ReviewReqDTO): Flow<ApiResult<ReviewResEntity>>
}