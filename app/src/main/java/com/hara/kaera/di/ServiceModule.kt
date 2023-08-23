package com.hara.kaera.di

import com.hara.kaera.BuildConfig
import com.hara.kaera.application.Constant
import com.hara.kaera.data.datasource.KaeraApi
import com.hara.kaera.data.util.ErrorHandlerImpl
import com.hara.kaera.domain.util.ErrorHandler
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

/*
    실제로 레트로핏을 통해서 api를 호출하기 위한 의존성들을 주입하는 모듈
    따라서 레트로핏 객체가 KaeraApi라는 인터페이스를 기반으로 만들어진다.
 */

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesHeaderInterceptor() = Interceptor { chain ->
        with(chain) {
            val request = request().newBuilder()
                .addHeader("Accept", Constant.APPLICATION_JSON)
                .addHeader("Authorization", BuildConfig.BEARER_TOKEN)
                .build()
            proceed(request)
        }
    }

    @Provides
    @Singleton
    fun providesLoggerInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        headerInterceptor: Interceptor,
        loggerInterceptor: HttpLoggingInterceptor
    ) =
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggerInterceptor)
            .build()


    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    @KAREARetrofit
    fun provideKAERARetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) //local.properties
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(Constant.APPLICATION_JSON.toMediaType()))
            .build()

    // 로깅인터셉터 달아서 레트로핏 객체를 제공하는 프로바이저

    @Qualifier
    annotation class KAREARetrofit

    @Singleton
    @Provides
    fun provideKaeraService(@KAREARetrofit kaeraService: Retrofit): KaeraApi {
        return kaeraService.create(KaeraApi::class.java)
    }

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler = ErrorHandlerImpl()

}