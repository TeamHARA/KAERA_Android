package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PutReviewUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    PutReviewUseCase {
    override fun invoke(reviewReqDTO: ReviewReqDTO): Flow<ApiResult<ReviewResEntity>> {
        return repository.updateReview(reviewReqDTO)
    }
}
