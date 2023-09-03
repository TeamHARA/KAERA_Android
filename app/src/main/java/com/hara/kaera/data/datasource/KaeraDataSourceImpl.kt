package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.DeleteWorryDTO
import com.hara.kaera.data.dto.HomeWorryListDTO
import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import com.hara.kaera.data.dto.WorryByTemplateDTO
import com.hara.kaera.data.dto.WorryDetailDTO
import com.hara.kaera.data.util.safeCallApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
실제로 KaeraApi 기반으로 만들어진 레트로핏 객체를 구현하는 곳
그래서 레트로핏객체를 주입받고 있다. 추가적으로 flow를 사용하므로
리턴타입으로 flow를 해주고 emit을 하는 형태로 리턴한다.
 */

class KaeraDataSourceImpl @Inject constructor(
    private val kaeraApi: KaeraApi
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
        }
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
}