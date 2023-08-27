package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.TemplateDetailEntity
import kotlinx.coroutines.flow.Flow

interface GetTemplateDetailUseCase{
    operator fun invoke(templateId: Int): Flow<ApiResult<TemplateDetailEntity>>
}