package com.hara.kaera.domain.repository

import com.hara.kaera.domain.entity.TemplateTypesEntity
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    fun getAllTemplateTypesInfo(): Flow<TemplateTypesEntity>

}