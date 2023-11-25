package com.hara.kaera.data.util

import com.hara.kaera.application.Constant
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.domain.repository.LoginRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class BaseInterceptor(
    private val loginRepository: LoginRepository,
    private val loginDataStore: LoginDataSource
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val currentToken = runBlocking {
                loginRepository.getSavedAccessToken().first()
            }

            val request = chain.request().newBuilder()
                .addHeader("Accept", Constant.APPLICATION_JSON)
                .addHeader("Authorization", currentToken)
                .build()

            var response = chain.proceed(request)
            // 401 코드 반환의 경우에만 토큰 갱신 후 데이터스토어에 저장 로직 수행
            if (response.code == 401) {
                val refreshToken = runBlocking {
                    loginRepository.getSavedRefreshToken().first()
                }

                val newToken = runBlocking {
                    refreshTokenIfNeeded(currentToken, refreshToken)
                }
                newToken?.let {
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Accept", Constant.APPLICATION_JSON)
                        .addHeader("Authorization", it)
                        .build()

                    response = chain.proceed(newRequest)
                }
            }

            return response
        }
    }

    private suspend fun refreshTokenIfNeeded(currentToken: String, refreshToken: String): String? {

        return try {
            val newToken = loginDataStore.getAccessToken(
                JWTRefreshReqDTO(
                    accessToken = currentToken,
                    refreshToken = refreshToken
                )
            ).first().data.accessToken

            newToken.let {
                loginRepository.updateAccessToken(accessToken = it)
                it
            }
        } catch (e: Exception) {
            null
        }
    }
}
