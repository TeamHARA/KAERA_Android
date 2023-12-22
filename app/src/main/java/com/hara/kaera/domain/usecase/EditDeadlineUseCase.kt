package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditDeadlineResDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import kotlinx.coroutines.flow.Flow

interface EditDeadlineUseCase {
    operator fun invoke(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<ApiResult<EditDeadlineResDTO>>
}