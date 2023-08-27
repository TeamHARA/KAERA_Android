package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.WorryByTemplateEntity
import kotlinx.coroutines.flow.Flow

interface GetWorryByTemplateUseCase {
    fun getStorageWorryFlow(templateId: Int): Flow<WorryByTemplateEntity>
}
