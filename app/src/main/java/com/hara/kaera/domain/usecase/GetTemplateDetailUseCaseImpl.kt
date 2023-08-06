package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetTemplateDetailUseCaseImpl  @Inject constructor(private val repository: KaeraRepository) :GetTemplateDetailUseCase{
    override fun getTemplateDetailFlow(templateId : Int): Flow<TemplateDetailEntity> {
        return flow {
            repository.getTemplateDetailInfo(templateId)
        }
    }
}