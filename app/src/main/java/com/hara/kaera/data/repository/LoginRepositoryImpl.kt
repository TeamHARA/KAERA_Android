package com.hara.kaera.data.repository

import androidx.datastore.core.DataStore
import com.hara.kaera.application.Constant
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.mapper.LoginMapper
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity
import com.hara.kaera.domain.entity.login.LoginData
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.domain.util.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteLoginDataSource: LoginDataSource,
    private val localLoginDataStore: DataStore<LoginData>,
    private val errorHandler: ErrorHandler,
) : LoginRepository {

    override fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>> {
        return flow {
            remoteLoginDataSource.getKakaoLoginJWT(accessToken).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(LoginMapper.mapperToJWT(it)))
            }
        }
    }

    override fun getUpdatedAccessToken(tokens: JWTRefreshReqDTO): Flow<ApiResult<String>> {
        return flow {
            remoteLoginDataSource.getAccessToken(tokens).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(LoginMapper.mapperToAccessToken(it)))
            }
        }
    }

    override suspend fun clearDataStore() {
        localLoginDataStore.updateData {
            it.copy(accessToken = null, refreshToken = null)
        }
    }

    override fun getSavedRefreshToken(): Flow<String> {
        return localLoginDataStore.data.catch {
            throw it
        }.map {
            it.refreshToken ?: Constant.EMPTY_TOKEN
        }
    }

    override fun getSavedAccessToken(): Flow<String> {
        return localLoginDataStore.data.catch {
            throw it
        }.map {
            it.accessToken ?: Constant.EMPTY_TOKEN
        }
    }

    override suspend fun updateAccessToken(accessToken: String) {
        localLoginDataStore.updateData {
            it.copy(accessToken = accessToken)
        }
    }

    override suspend fun saveKaeraJWT(accessToken: String, refreshToken: String) {
        localLoginDataStore.updateData {
            it.copy(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

}