package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.domain.entity.EditDeadlineEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditDeadlineUseCaseImpl @Inject constructor(private val repository: KaeraRepository)
: EditDeadlineUseCase {

    override fun invoke(editDeadlineReqDTO: EditDeadlineReqDTO): Flow<ApiResult<EditDeadlineEntity>> {
        return repository.editDeadline(editDeadlineReqDTO)
    }
}