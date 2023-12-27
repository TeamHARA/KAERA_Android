package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryResDTO
import com.hara.kaera.domain.entity.ReviewResEntity
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteWorryUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    WriteWorryUseCase {
    override fun invoke(writeWorryReqDTO: WriteWorryReqDTO): Flow<ApiResult<WorryDetailEntity>> {
        return repository.writeWorry(writeWorryReqDTO)
    }
}