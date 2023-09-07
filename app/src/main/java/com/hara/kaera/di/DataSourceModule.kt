package com.hara.kaera.di

import com.hara.kaera.data.datasource.KaeraApi
import com.hara.kaera.data.datasource.KaeraDataSource
import com.hara.kaera.data.datasource.KaeraDataSourceImpl
import com.hara.kaera.data.datasource.LoginApi
import com.hara.kaera.data.datasource.LoginDataSource
import com.hara.kaera.data.datasource.LoginDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    원래 DataSoure는 remote와 local로 나누어진다 하지만 우리는 없기때문에 현재 
    remote부분의 실제 kaeraApi를 사용해야 하는 retrofit 객체를 주입받아서 만들어진 DataSource를
    레포짓토리에다가 주입하기 위한 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideKaeraDataSource(
        @ServiceModule.KAREARetrofit kaeraApi: KaeraApi
    ): KaeraDataSource {
        return KaeraDataSourceImpl(kaeraApi)
    }

    @Provides
    @Singleton
    fun provideLoginDataSource(
        @ServiceModule.LoginRetrofit loginApi: LoginApi
    ): LoginDataSource {
        return LoginDataSourceImpl(loginApi)
    }

}