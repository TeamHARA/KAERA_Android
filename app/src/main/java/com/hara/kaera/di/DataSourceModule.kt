package com.hara.kaera.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hara.kaera.application.Constant
import com.hara.kaera.data.datasource.remote.KaeraApi
import com.hara.kaera.data.datasource.remote.KaeraDataSource
import com.hara.kaera.data.datasource.remote.KaeraDataSourceImpl
import com.hara.kaera.data.datasource.remote.LoginApi
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.data.datasource.remote.LoginDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    원래 DataSoure는 remote와 local로 나누어진다 하지만 우리는 없기때문에 현재 
    remote부분의 실제 kaeraApi를 사용해야 하는 retrofit 객체를 주입받아서 만들어진 DataSource를
    레포짓토리에다가 주입하기 위한 모듈
 */

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constant.TOKEN_DATASTORE_NAME
) // 싱클톤 생성을 위해서 top-level에서 생성

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

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.tokenDataStore
    }

}