package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.WorryByTemplateEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorryByTemplateUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    GetWorryByTemplateUseCase {
    override fun getStorageWorryFlow(templateId: Int): Flow<WorryByTemplateEntity> {
        return repository.getWorryByTemplate(templateId)
    }
}
