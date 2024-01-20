package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditWorryUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    EditWorryUseCase {

    override fun invoke(editWorryReqDTO: EditWorryReqDTO): Flow<ApiResult<String>> {
        return repository.editWorry(editWorryReqDTO)
    }
}