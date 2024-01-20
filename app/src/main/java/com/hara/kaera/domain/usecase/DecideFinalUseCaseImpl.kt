package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DecideFinalUseCaseImpl @Inject constructor(private val repository: KaeraRepository)
: DecideFinalUseCase {

    override fun invoke(decideFinalReqDTO: DecideFinalReqDTO): Flow<ApiResult<String>> {
        return repository.decideFinal(decideFinalReqDTO)
    }
}