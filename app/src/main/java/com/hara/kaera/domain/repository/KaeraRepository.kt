package com.hara.kaera.domain.repository

import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    fun getAllTemplateTypesInfo(): Flow<TemplateTypesEntity>

    fun getTemplateDetailInfo(templateId: Int): Flow<TemplateDetailEntity>

    fun getWorryByTemplate(templateId: Int): Flow<WorryByTemplateEntity>
}
