package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.DeleteWorryEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteWorryUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    DeleteWorryUseCase {
    override fun invoke(worryId: Int): Flow<ApiResult<DeleteWorryEntity>> {
        return repository.deleteWorry(worryId)
    }
}
