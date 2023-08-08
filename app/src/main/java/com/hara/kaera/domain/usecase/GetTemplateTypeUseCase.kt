package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.TemplateTypesEntity
import kotlinx.coroutines.flow.Flow

interface GetTemplateTypeUseCase {
    fun getTemplateFlow(): Flow<TemplateTypesEntity>
}