package com.hara.kaera.data.util

import com.hara.kaera.BuildConfig
import com.hara.kaera.application.Constant
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.domain.dto.JWTRefreshReqDTO
import com.hara.kaera.domain.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


class BaseInterceptor (
    private val loginRepository: LoginRepository,
    private val loginDataStore: LoginDataSource
) : Interceptor {

    val authToken: String = runBlocking {
        loginRepository.getSavedAccessToken().first()
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Accept", Constant.APPLICATION_JSON)
            .addHeader("Authorization", authToken ?: "nothing")
            //.addHeader("Authorization", BuildConfig.BEARER_TOKEN)
            .build()

        var response = chain.proceed(request)

        if (response.code == 401) {
            val refreshToken: String = runBlocking {
                loginRepository.getSavedRefreshToken().first()
            }
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    loginDataStore.getAccessToken(
                        JWTRefreshReqDTO(
                            accessToken = authToken,
                            refreshToken = refreshToken
                        )
                    )
                }
                    .onSuccess {
                        it.collect {
                            loginRepository.updateAccessToken(accessToken = it.data.accessToken)
                        }
                    }.onFailure {
                        throw it
                    }
            }

            val newAuthToken: String = runBlocking {
                loginRepository.getSavedAccessToken().first()
            }

            val newRequest = chain.request().newBuilder()
                .addHeader("Accept", Constant.APPLICATION_JSON)
                .addHeader("Authorization", newAuthToken ?: "nothing")
                .build()
            response = chain.proceed(newRequest)
        }

        return response

    }
}