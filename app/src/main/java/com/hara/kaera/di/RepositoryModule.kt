package com.hara.kaera.di

import com.hara.kaera.data.repository.KaeraRepositoryImpl
import com.hara.kaera.domain.repository.KaeraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    레포짓토리를 실제로 사용할 유즈케이스에다가 주입해주기 위한 모듈
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindToKaeraRepository(kaeraRepositoryImpl: KaeraRepositoryImpl): KaeraRepository
}