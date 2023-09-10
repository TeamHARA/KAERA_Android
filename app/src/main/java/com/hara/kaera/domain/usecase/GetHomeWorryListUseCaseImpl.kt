package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeWorryListUseCaseImpl @Inject constructor(private val repository: KaeraRepository): GetHomeWorryListUseCase {
    override fun invoke(isSolved: Int): Flow<ApiResult<HomeWorryListEntity>> {
        return repository.getHomeWorryList(isSolved)
    }
}