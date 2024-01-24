package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.WorryDetailEntity
import kotlinx.coroutines.flow.Flow

interface WriteWorryUseCase {
    operator fun invoke(writeWorryReqDTO: WriteWorryReqDTO): Flow<ApiResult<WorryDetailEntity>>
}