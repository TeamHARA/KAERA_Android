package com.hara.kaera.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.hara.kaera.application.Constant
import com.hara.kaera.data.datasource.local.LoginDataStoreSerializer
import com.hara.kaera.data.datasource.remote.KaeraApi
import com.hara.kaera.data.datasource.remote.KaeraDataSource
import com.hara.kaera.data.datasource.remote.KaeraDataSourceImpl
import com.hara.kaera.data.datasource.remote.LoginApi
import com.hara.kaera.data.datasource.remote.LoginDataSource
import com.hara.kaera.data.datasource.remote.LoginDataSourceImpl
import com.hara.kaera.data.util.CryptoManager
import com.hara.kaera.domain.entity.login.LoginData
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
    fun provideCryptoManger(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun provideLoginDataStoreSerializer(
        cryptoManager: CryptoManager
    ): LoginDataStoreSerializer = LoginDataStoreSerializer(cryptoManager)

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
        loginDataStoreSerializer: LoginDataStoreSerializer
    ): DataStore<LoginData> {
        return DataStoreFactory.create(
            serializer = loginDataStoreSerializer,
            produceFile = { context.dataStoreFile(Constant.TOKEN_DATASTORE_NAME) },
            corruptionHandler = null
        )
    }

}