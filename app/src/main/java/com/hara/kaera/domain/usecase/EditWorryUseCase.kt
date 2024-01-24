package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditWorryReqDTO
import kotlinx.coroutines.flow.Flow

interface EditWorryUseCase {
    operator fun invoke(editWorryReqDTO: EditWorryReqDTO): Flow<ApiResult<String>>
}