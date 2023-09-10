package com.hara.kaera.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.hara.kaera.application.Constant
import com.hara.kaera.application.Constant.EMPTY_TOKEN
import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.mapper.Mapper
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.LoginRepository
import com.hara.kaera.domain.util.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteLoginDataSource: LoginDataSource,
    private val localLoginDataStore: DataStore<Preferences>,
    private val errorHandler: ErrorHandler
) : LoginRepository {

    override fun getKakaoLoginJTW(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>> {
        return flow {
            remoteLoginDataSource.getKakaoLoginJWT(accessToken).catch {
                emit(ApiResult.Error(errorHandler(it)))
            }.collect {
                emit(ApiResult.Success(Mapper.mapperToJWT(it)))
            }
        }
    }

    override suspend fun clearDataStore() {
        localLoginDataStore.edit { it.clear() }
    }

    override fun getSavedAccessToken(): Flow<String> {
        return localLoginDataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[Constant.ACCESS_TOKEN_KEY] ?: EMPTY_TOKEN
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        localLoginDataStore.edit {
            it[Constant.ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        localLoginDataStore.edit {
            it[Constant.REFRESH_TOKEN_KEY] = refreshToken
        }
    }
}