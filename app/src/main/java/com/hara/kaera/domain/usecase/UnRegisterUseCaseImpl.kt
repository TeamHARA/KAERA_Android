package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnRegisterUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    UnRegisterUseCase {
    override fun invoke(): Flow<ApiResult<Boolean>> {
        return repository.serviceLogout()
    }
}