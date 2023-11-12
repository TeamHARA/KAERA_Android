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
import com.hara.kaera.data.util.safeCallApi
import com.hara.kaera.di.ServiceModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
실제로 KaeraApi 기반으로 만들어진 레트로핏 객체를 구현하는 곳
그래서 레트로핏객체를 주입받고 있다. 추가적으로 flow를 사용하므로
리턴타입으로 flow를 해주고 emit을 하는 형태로 리턴한다.
 */

class KaeraDataSourceImpl @Inject constructor(
    @ServiceModule.KAREARetrofit private val kaeraApi: KaeraApi
) : KaeraDataSource {

    override fun getTemplateTypesInfo(): Flow<TemplateTypeDTO> {
        return flow {
            emit(kaeraApi.getTemplateTypesInfo())
        }.safeCallApi()
    }

    override fun getTemplateDetail(templateId: Int): Flow<TemplateDetailDTO> {
        return flow {
            emit(kaeraApi.getTemplateDetail(templateId))
        }.safeCallApi()
    }

    override fun getHomeWorryList(isSolved: Int): Flow<HomeWorryListDTO> {
        return flow {
            emit(kaeraApi.getHomeWorryList(isSolved))
        }.safeCallApi()
    }

    override fun getWorryByTemplate(templateId: Int): Flow<WorryByTemplateDTO> {
        return flow {
            emit(kaeraApi.getWorryByTemplate(templateId))
        }.safeCallApi()
    }

    override fun getWorryDetail(worryId: Int): Flow<WorryDetailDTO> {
        return flow {
            emit(kaeraApi.getWorryDetail(worryId))
        }.safeCallApi()
    }

    override fun deleteWorryById(worryId: Int): Flow<DeleteWorryDTO> {
        return flow {
            emit(kaeraApi.deleteWorry(worryId))
        }.safeCallApi()
    }

    override fun updateReview(reviewReqDTO: ReviewReqDTO): Flow<ReviewResDTO> {
        return flow {
            emit(kaeraApi.updateReview(reviewReqDTO))
        }.safeCallApi()
    }

    override fun editWorry(editWorryReqDTO: EditWorryReqDTO): Flow<EditWorryResDTO> {
        return flow {
            emit(kaeraApi.editWorry(editWorryReqDTO))
        }.safeCallApi()
    }

    override fun editDeadline(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<EditDeadlineResDTO> {
        return flow {
            emit(kaeraApi.editDeadline(editDeadlineReqDTO))
        }.safeCallApi()
    }


    override fun writeWorry(writeWorryReqDTO: WriteWorryReqDTO): Flow<WriteWorryResDTO> {
        return flow {
            emit(kaeraApi.writeWorry(writeWorryReqDTO))
        }
    }

    override fun decideFinal(decideFinalReqDTO: DecideFinalReqDTO): Flow<DecideFinalResDTO> {
        return flow {
            emit(kaeraApi.decideFinal(decideFinalReqDTO))
        }
    }
}