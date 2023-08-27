package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import kotlinx.coroutines.flow.Flow

interface GetWorryByTemplateUseCase {
    operator fun invoke(templateId: Int): Flow<ApiResult<WorryByTemplateEntity>>
}
