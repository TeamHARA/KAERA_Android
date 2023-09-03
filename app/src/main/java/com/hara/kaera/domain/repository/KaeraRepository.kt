package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    fun getAllTemplateTypesInfo(): Flow<ApiResult<TemplateTypesEntity>>

    fun getTemplateDetailInfo(templateId: Int): Flow<ApiResult<TemplateDetailEntity>>

    fun getHomeWorryList(isSolved: Int): Flow<HomeWorryListEntity>

    fun getWorryByTemplate(templateId: Int): Flow<ApiResult<WorryByTemplateEntity>>

    fun getWorryDetail(worryId: Int): Flow<ApiResult<WorryDetailEntity>>

    fun deleteWorry(worryId: Int): Flow<ApiResult<DeleteWorryEntity>>
}
