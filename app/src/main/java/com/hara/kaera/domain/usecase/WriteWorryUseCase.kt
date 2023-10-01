package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.ReviewResEntity
import kotlinx.coroutines.flow.Flow

interface WriteWorryUseCase {
    operator fun invoke(writeWorryReqDTO: WriteWorryReqDTO): Flow<ApiResult<String>>
}