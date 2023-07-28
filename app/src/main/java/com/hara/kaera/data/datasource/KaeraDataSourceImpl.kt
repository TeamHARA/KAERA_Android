package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.TemplateTypeDTO
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
        }
    }
}