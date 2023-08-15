package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.HomeWorryListEntity
import kotlinx.coroutines.flow.Flow

interface GetHomeWorryListUseCase {
    fun getHomeWorryListFlow(isSolved: Int): Flow<HomeWorryListEntity>
}