package com.hara.kaera.data.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.datasource.remote.KaeraDataSource
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.DecideFinalResDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.EditWorryResDTO
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.data.mapper.Mapper.mapperToDeleteWorry
import com.hara.kaera.data.mapper.Mapper.mapperToHomeWorryList
import com.hara.kaera.data.mapper.Mapper.mapperToReview
import com.hara.kaera.data.mapper.Mapper.mapperToStorageWorry
import com.hara.kaera.data.mapper.Mapper.mapperToTemplateDetail
import com.hara.kaera.data.mapper.Mapper.mapperToTemplateType
import com.hara.kaera.data.mapper.Mapper.mapperToWorryDetail
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.repository.KaeraRepository
import com.hara.kaera.domain.util.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
    private val kaeraDataSource: KaeraDataSource,
    private val errorHandler: ErrorHandler,
) : KaeraRepository {

    override fun getAllTemplateTypesInfo(): Flow<ApiResult<TemplateTypesEntity>> {
        return flow {
            kaeraDataSource.getTemplateTypesInfo().catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToTemplateType(it)))
            }
        }
    }

    override fun getTemplateDetailInfo(templateId: Int): Flow<ApiResult<TemplateDetailEntity>> {
        return flow {
            kaeraDataSource.getTemplateDetail(templateId).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToTemplateDetail(it)))
            }
        }
    }

    override fun getHomeWorryList(isSolved: Int): Flow<ApiResult<HomeWorryListEntity>> {
        return flow {
            kaeraDataSource.getHomeWorryList(isSolved).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToHomeWorryList(it)))
            }
        }
    }

    override fun getWorryByTemplate(templateId: Int): Flow<ApiResult<WorryByTemplateEntity>> {
        return flow {
            kaeraDataSource.getWorryByTemplate(templateId).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToStorageWorry(it)))
            }
        }
    }

    override fun getWorryDetail(worryId: Int): Flow<ApiResult<WorryDetailEntity>> {
        return flow {
            kaeraDataSource.getWorryDetail(worryId).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToWorryDetail(it)))
            }
        }
    }

    override fun deleteWorry(worryId: Int): Flow<ApiResult<DeleteWorryEntity>> {
        return flow {
            kaeraDataSource.deleteWorryById(worryId).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToDeleteWorry(it)))
            }
        }
    }

    override fun updateReview(reviewReqDTO: ReviewReqDTO): Flow<ApiResult<ReviewResEntity>> {
        return flow {
            kaeraDataSource.updateReview(reviewReqDTO).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(mapperToReview(it)))
            }
        }
    }

    // TODO: Nothing으로 바꾸어서 넘겨줘야 할 듯.. 현재는 그냥 (임시적으로) updatedAt(날짜/String) 넘겨줘
    override fun editWorry(editWorryReqDTO: EditWorryReqDTO): Flow<ApiResult<String>> {
        return flow {
            kaeraDataSource.editWorry(editWorryReqDTO).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(it.data.updatedAt))
            }
        }
    }

    // TODO: Nothing으로 바꾸어서 넘겨줘야 할 듯.. 현재는 그냥 (임시적으로) deadline(날짜/String) 넘겨줘
    override fun editDeadline(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<ApiResult<String>> {
        return flow {
            kaeraDataSource.editDeadline(editDeadlineReqDTO).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(it.data.deadline))
            }
        }
    }


    // TODO: Nothing으로 바꾸어서 넘겨줘야 할 듯.. 현재는 그냥 (임시적으로) message 넘겨줘
    override fun writeWorry(writeWorryReqDTO: WriteWorryReqDTO): Flow<ApiResult<String>> {
        return flow {
            kaeraDataSource.writeWorry(writeWorryReqDTO).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(it.message))
            }
        }
    }

    override fun decideFinal(decideFinalReqDTO: DecideFinalReqDTO): Flow<ApiResult<String>> {
        return flow {
            kaeraDataSource.decideFinal(decideFinalReqDTO).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(it.data.quote))
            }
        }
    }
}
