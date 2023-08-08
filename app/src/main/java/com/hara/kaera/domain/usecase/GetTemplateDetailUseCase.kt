package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.TemplateDetailEntity
import kotlinx.coroutines.flow.Flow

interface GetTemplateDetailUseCase{
    fun getTemplateDetailFlow(templateId: Int): Flow<TemplateDetailEntity>
}