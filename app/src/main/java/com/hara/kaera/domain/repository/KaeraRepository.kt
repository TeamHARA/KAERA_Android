package com.hara.kaera.domain.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.PushAlarmReqDTO
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.EditDeadlineEntity
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import kotlinx.coroutines.flow.Flow

interface KaeraRepository {

    fun getAllTemplateTypesInfo(): Flow<ApiResult<TemplateTypesEntity>>

    fun getTemplateDetailInfo(templateId: Int): Flow<ApiResult<TemplateDetailEntity>>

    fun getHomeWorryList(isSolved: Int, page: Int, limit: Int): Flow<ApiResult<HomeWorryListEntity>>

    fun getWorryByTemplate(templateId: Int): Flow<ApiResult<WorryByTemplateEntity>>

    fun getWorryDetail(worryId: Int): Flow<ApiResult<WorryDetailEntity>>

    fun deleteWorry(worryId: Int): Flow<ApiResult<DeleteWorryEntity>>

    fun updateReview(reviewReqDTO: ReviewReqDTO): Flow<ApiResult<ReviewResEntity>>

    fun editWorry(editWorryReqDTO: EditWorryReqDTO): Flow<ApiResult<String>>

    fun editDeadline(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<ApiResult<EditDeadlineEntity>>

    fun writeWorry(writeWorryReqDTO: WriteWorryReqDTO): Flow<ApiResult<WorryDetailEntity>>

    fun decideFinal(decideFinalReqDTO: DecideFinalReqDTO): Flow<ApiResult<String>>

    fun serviceLogout(): Flow<ApiResult<Boolean>>

    fun serviceUnRegister(): Flow<ApiResult<Boolean>>

    fun pushAlarmEnabled(isTrued: Int, pushAlarmReqDTO: PushAlarmReqDTO): Flow<ApiResult<String>>
}
