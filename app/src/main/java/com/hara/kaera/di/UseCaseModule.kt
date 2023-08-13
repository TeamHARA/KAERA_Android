package com.hara.kaera.di

import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCaseImpl
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/*
    유즈케이스를 실제로 사용하는 뷰모델에 주입해주기 위한 모듈
    따라서 ViewModelScoped라는 어노테이션을 붙여줌
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindToGetTemplateType(getTemplateTypeUseCase: GetTemplateTypeUseCaseImpl): GetTemplateTypeUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToGetTemplateDetail(getTemplateDetailUseCase: GetTemplateDetailUseCaseImpl) : GetTemplateDetailUseCase

}