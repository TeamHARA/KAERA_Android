package com.hara.kaera.data.repository

import com.hara.kaera.data.datasource.KaeraService
import com.hara.kaera.data.data.TemplateTypeDTO
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KaeraRepositoryImpl @Inject constructor(
    private val kaeraService: KaeraService
) : KaeraRepository{

    override suspend fun getAllTemplateTypesInfo(): Flow<TemplateTypeDTO> {
        return flow {
            emit(kaeraService.getTemplateTypesInfo())
        }
    }
}