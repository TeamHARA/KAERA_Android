package com.hara.kaera.domain.repository

import com.hara.kaera.data.data.TemplateTypeDTO
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    suspend fun getAllTemplateTypesInfo(): Flow<TemplateTypeDTO>

}