package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeWorryListUseCaseImpl @Inject constructor(private val repository: KaeraRepository): GetHomeWorryListUseCase {
    override fun getHomeWorryListFlow(isSolved: Int): Flow<HomeWorryListEntity> {
        return repository.getHomeWorryList(isSolved)
    }
}