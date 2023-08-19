package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTemplateDetailUseCaseImpl  @Inject constructor(private val repository: KaeraRepository) :GetTemplateDetailUseCase{
    override fun invoke(templateId : Int): Flow<TemplateDetailEntity> {
        return repository.getTemplateDetailInfo(templateId)
    }
}