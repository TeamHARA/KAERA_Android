package com.hara.kaera.di

import com.hara.kaera.domain.usecase.DeleteWorryUseCase
import com.hara.kaera.domain.usecase.DeleteWorryUseCaseImpl
import com.hara.kaera.domain.usecase.EditDeadlineUseCase
import com.hara.kaera.domain.usecase.EditDeadlineUseCaseImpl
import com.hara.kaera.domain.usecase.EditWorryUseCase
import com.hara.kaera.domain.usecase.EditWorryUseCaseImpl
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCase
import com.hara.kaera.domain.usecase.GetHomeWorryListUseCaseImpl
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCase
import com.hara.kaera.domain.usecase.GetTemplateDetailUseCaseImpl
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCase
import com.hara.kaera.domain.usecase.GetTemplateTypeUseCaseImpl
import com.hara.kaera.domain.usecase.GetWorryByTemplateUseCase
import com.hara.kaera.domain.usecase.GetWorryByTemplateUseCaseImpl
import com.hara.kaera.domain.usecase.GetWorryDetailUseCase
import com.hara.kaera.domain.usecase.GetWorryDetailUseCaseImpl
import com.hara.kaera.domain.usecase.PutReviewUseCase
import com.hara.kaera.domain.usecase.PutReviewUseCaseImpl
import com.hara.kaera.domain.usecase.WriteWorryUseCase
import com.hara.kaera.domain.usecase.WriteWorryUseCaseImpl
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
    abstract fun bindToGetTemplateDetail(getTemplateDetailUseCase: GetTemplateDetailUseCaseImpl): GetTemplateDetailUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToGetHomeWorryList(getHomeWorryListUseCase: GetHomeWorryListUseCaseImpl): GetHomeWorryListUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToGetStorageWorry(getWorryByTemplateUseCase: GetWorryByTemplateUseCaseImpl): GetWorryByTemplateUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToWorryDetail(getWorryDetailUseCase: GetWorryDetailUseCaseImpl): GetWorryDetailUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToDeleteWorry(deleteWorryUseCase: DeleteWorryUseCaseImpl): DeleteWorryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToReview(putReviewUseCase: PutReviewUseCaseImpl): PutReviewUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToEditWorry(editWorryUseCase: EditWorryUseCaseImpl): EditWorryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToEditDeadline(editDeadlineUseCase: EditDeadlineUseCaseImpl): EditDeadlineUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindToWriteWorry(writeWorryUseCase: WriteWorryUseCaseImpl): WriteWorryUseCase
}
