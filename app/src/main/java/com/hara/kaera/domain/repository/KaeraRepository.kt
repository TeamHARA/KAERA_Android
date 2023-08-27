package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    fun getAllTemplateTypesInfo(): Flow<ApiResult<TemplateTypesEntity>>

    fun getTemplateDetailInfo(templateId: Int): Flow<ApiResult<TemplateDetailEntity>>

    fun getHomeWorryList(isSolved: Int): Flow<HomeWorryListEntity>

    fun getWorryByTemplate(templateId: Int): Flow<WorryByTemplateEntity>
}
