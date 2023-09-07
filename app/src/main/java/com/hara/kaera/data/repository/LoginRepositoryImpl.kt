package com.hara.kaera.data.repository

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.datasource.LoginDataSource
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.mapper.Mapper
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.domain.util.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource,
    private val errorHandler: ErrorHandler
) : LoginRepository {

    override fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>> {
        return flow {
            loginDataSource.getKakaoLoginJWT(accessToken).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(Mapper.mapperToJWT(it)))
            }
        }
    }
}