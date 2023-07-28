package com.hara.kaera.data.repository

import com.hara.kaera.data.datasource.KaeraDataSource
import com.hara.kaera.data.mapper.Mapper.mapperToTemplateType
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
  실제로 DataSource를 호출하여 데이터를 가져오는 레포짓토리
  해라에서는 DTO를 그대로 가져다 썼기때문에 의미가 없었지만
  Mapper를 통해 데이터 가공을 해주어서 DTO타입이 아닌
  TemplateEntity 타입의 Flow를  emit해준다

  정석대로라면 local/remote datasource를 둘다 주입받겠지만 local이
  없으므로 remote datasource만 주입
 */

class KaeraRepositoryImpl @Inject constructor(
    private val kaeraDataSource: KaeraDataSource
) : KaeraRepository {

    override fun getAllTemplateTypesInfo(): Flow<TemplateTypesEntity> {
        return flow {
            kaeraDataSource.getTemplateTypesInfo().collect {
                emit(mapperToTemplateType(it))
            }
        }
    }
}