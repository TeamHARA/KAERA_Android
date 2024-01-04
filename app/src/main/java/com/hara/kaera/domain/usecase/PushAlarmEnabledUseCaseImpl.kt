package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PushAlarmEnabledUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    PushAlarmEnabledUseCase {
    override fun invoke(isTrued: Int, pushAlarmReqDTO: String): Flow<ApiResult<String>> {
        return repository.pushAlarmEnabled(isTrued, pushAlarmReqDTO)
    }
}