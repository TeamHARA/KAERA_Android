package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorryDetailUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    GetWorryDetailUseCase {

    override fun invoke(worryId: Int): Flow<ApiResult<WorryDetailEntity>> {
        return repository.getWorryDetail(worryId)
    }
}
