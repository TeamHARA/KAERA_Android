package com.hara.kaera.data.datasource.remote

import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.DecideFinalResDTO
import com.hara.kaera.data.dto.DeleteWorryDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditDeadlineResDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.EditWorryResDTO
import com.hara.kaera.data.dto.HomeWorryListDTO
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.ReviewResDTO
import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import com.hara.kaera.data.dto.WorryByTemplateDTO
import com.hara.kaera.data.dto.WorryDetailDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryResDTO
import com.hara.kaera.data.dto.login.ServiceDisConnectResDTO
import kotlinx.coroutines.flow.Flow

/*
    실제로 데이터를 받아오는곳 여기서 status코드로 현재 서버상태를 판별하는게 정석적이지만
    그것 나중에 하도록 하고 정석대로라면 remote / local의 datasoure가 각각 있을것 하지만
    우리는 local은 없으므로 remote만 있는 상태
 */

interface KaeraDataSource {

    fun getTemplateTypesInfo(): Flow<TemplateTypeDTO>

    fun getTemplateDetail(templateId: Int): Flow<TemplateDetailDTO>

    fun getHomeWorryList(isSolved: Int): Flow<HomeWorryListDTO>

    fun getWorryByTemplate(templateId: Int): Flow<WorryByTemplateDTO>

    fun getWorryDetail(worryId: Int): Flow<WorryDetailDTO>

    fun deleteWorryById(worryId: Int): Flow<DeleteWorryDTO>

    fun updateReview(reviewReqDTO: ReviewReqDTO): Flow<ReviewResDTO>

    fun editWorry(editWorryReqDTO: EditWorryReqDTO): Flow<EditWorryResDTO>

    fun editDeadline(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<EditDeadlineResDTO>

    fun writeWorry(writeWorryReqDTO: WriteWorryReqDTO): Flow<WriteWorryResDTO>

    fun decideFinal(decideFinalReqDTO: DecideFinalReqDTO): Flow<DecideFinalResDTO>

    fun serviceLogout(): Flow<ServiceDisConnectResDTO>

    fun serviceUnRegister(): Flow<ServiceDisConnectResDTO>
}
